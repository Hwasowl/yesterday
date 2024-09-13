package se.sowl.yesterdayai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.sowl.yesterdayai.dto.BingSearchResponse;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BingSearchService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${azure.bing-search.api-key}")
    private String bingSearchApiKey;

    public List<BingSearchResponse> getYesterdayNews() {
        HttpEntity<String> header = generateHttpHeader();
        String searchUrl = "https://api.bing.microsoft.com/v7.0/news/search?q=한국 주요뉴스&freshness=Day&sortBy=Date&count=3&mkt=ko-KR";
        return search(searchUrl, header).stream().map(BingSearchResponse::new).toList();
    }

    private List<Map<String, String>> search(String bingSearchUrl, HttpEntity<String> header) {
        ResponseEntity<Map> response = restTemplate.exchange(bingSearchUrl, HttpMethod.GET, header, Map.class);
        return (List<Map<String, String>>) response.getBody().get("value");
    }

    private HttpEntity<String> generateHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", bingSearchApiKey);
        return new HttpEntity<>(headers);
    }
}

