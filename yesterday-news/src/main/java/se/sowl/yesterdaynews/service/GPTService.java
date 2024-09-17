package se.sowl.yesterdaynews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.api-url}")
    private String apiUrl;

    public List<String> summarizeAndTagNews(List<String> newsItems) {
        HttpHeaders headers = generateHttpHeader();
        Map<String, Object> requestBody = generateRequestBody(newsItems);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            return summarizeAndTag(request);
        } catch (Exception e) {
            log.error("GPT로 요약 및 태그 생성 중 문제가 발생했습니다.", e);
            throw new RuntimeException("GPT 처리 중 오류 발생", e);
        }
    }

    private List<String> summarizeAndTag(HttpEntity<Map<String, Object>> request) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String content = jsonNode.path("choices").get(0).path("message").path("content").asText().trim();
        return Arrays.asList(content.split("\n---\n"));
    }

    private Map<String, Object> generateRequestBody(List<String> newsItems) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo-0125");

        StringBuilder prompt = new StringBuilder("다음 뉴스 각각에 대해 한 문장으로 요약하고 관련 태그를 생성하세요. 각 뉴스의 응답을 '---'로 구분하세요.\n\n");
        for (int i = 0; i < newsItems.size(); i++) {
            prompt.append("뉴스 ").append(i + 1).append(":\n").append(newsItems.get(i)).append("\n\n");
        }

        requestBody.put("messages", new Object[]{
            Map.of("role", "system", "content", "당신은 뉴스 요약 및 태그 생성 전문가입니다. 각 뉴스에 대해 '요약: [한 문장 요약]\\n태그: [관련 태그들]' 형식으로 응답하세요."),
            Map.of("role", "user", "content", prompt.toString())
        });
        requestBody.put("max_tokens", 2000);
        requestBody.put("temperature", 0.7);
        return requestBody;
    }

    private HttpHeaders generateHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiApiKey);
        return headers;
    }
}
