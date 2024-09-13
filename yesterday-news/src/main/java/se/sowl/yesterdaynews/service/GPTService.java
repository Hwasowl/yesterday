package se.sowl.yesterdaynews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${openai.api-key}")
    private String openAiApiKey;
    @Value("${openai.api-url}")
    private String apiUrl;

    public String summarizeText(String text) {
        HttpHeaders headers = generateHttpHeader();
        Map<String, Object> requestBody = generateRequestBody(text);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            return summarize(request);
        } catch (Exception e) {
            throw new RuntimeException("GPT로 요약하는 도중 문제가 생겼습니다.", e);
        }
    }

    private String summarize(HttpEntity<Map<String, Object>> request) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.path("choices").get(0).path("message").path("content").asText().trim();
    }

    private static Map<String, Object> generateRequestBody(String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo-0125");
        requestBody.put("messages", new Object[]{
            Map.of("role", "system", "content", "이건 내가 작성한 뉴스 기사야."),
            Map.of("role", "user", "content", "너가 핵심을 요약해서 3문장으로 나눠서 줘.:\n\n" + text)
        });
        requestBody.put("max_tokens", 150);
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
