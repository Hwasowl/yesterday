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

    @Value("${openai.retry-count}")
    private int maxRetries;


    public List<String> summarizeAndTagNews(List<String> newsItems) {
        List<String> results = new ArrayList<>();
        for (String newsItem : newsItems) {
            String result = summarizeAndTagSingleNews(newsItem);
            results.add(result);
        }
        return results;
    }

    private String summarizeAndTagSingleNews(String newsItem) {
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                String result = attemptSummarizeAndTagSingleNews(newsItem);
                if (validateResult(result)) {
                    return result;
                }
                log.warn("형식에 맞지 않는 뉴스 재시도 처리합니다. 횟수: {}", attempt + 1);
            } catch (Exception e) {
                log.error("뉴스 가공 실패. 횟수 {}: {}", attempt + 1, e.getMessage());
            }
        }
        log.error("재시도 횟수 안에 가공에 실패했습니다. 횟수 {}", maxRetries);
        return "요약: 처리 실패\n태그: 오류";
    }

    private String attemptSummarizeAndTagSingleNews(String newsItem) throws JsonProcessingException {
        HttpHeaders headers = generateHttpHeader();
        Map<String, Object> requestBody = generateRequestBody(Collections.singletonList(newsItem));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.path("choices").get(0).path("message").path("content").asText().trim();
    }

    private boolean validateResult(String result) {
        String[] parts = result.split("\n");
        return parts.length >= 2 &&
            parts[0].startsWith("요약:") &&
            parts[1].startsWith("태그:") &&
            !parts[0].equals("요약:") &&
            !parts[1].equals("태그:");
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
