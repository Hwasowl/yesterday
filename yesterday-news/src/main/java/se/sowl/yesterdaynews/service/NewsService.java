package se.sowl.yesterdaynews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaynews.dto.BingSearchResponse;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaydomain.news.repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final BingSearchService bingSearchService;
    private final GPTService gptService;
    private final NewsRepository newsRepository;

    public List<News> getYesterdayNews() {
        return newsRepository.findAll();
    }

    public void saveYesterdayNews() {
        List<BingSearchResponse> searchResults = bingSearchService.getYesterdayNews();
        log.info("{} 개의 뉴스를 조회했습니다.", searchResults.size());
        List<News> processedNews = processSearchResults(searchResults);
        log.info("{} 개의 뉴스를 요약했습니다.", processedNews.size());
        newsRepository.saveAll(processedNews);
    }

    private List<News> processSearchResults(List<BingSearchResponse> searchResults) {
        List<String> newsItems = searchResults.stream()
            .map(this::formatNewsItem)
            .collect(Collectors.toList());

        List<String> summariesAndTags = gptService.summarizeAndTagNews(newsItems);

        log.info("검색 결과 수: {}, 요약 및 태그 수: {}", searchResults.size(), summariesAndTags.size());

        List<News> processedNews = new ArrayList<>();
        for (int i = 0; i < searchResults.size(); i++) {
            BingSearchResponse response = searchResults.get(i);
            if (i < summariesAndTags.size()) {
                String result = summariesAndTags.get(i).trim();

                String summary = "";
                String tag = "";

                if (result.contains("요약:") && result.contains("태그:")) {
                    summary = result.substring(result.indexOf("요약:") + 3, result.indexOf("태그:")).trim();
                    tag = result.substring(result.indexOf("태그:") + 3).trim();
                }

                if (!summary.isEmpty() && !tag.isEmpty()) {
                    processedNews.add(convertToNews(response, summary, tag));
                } else {
                    log.warn("뉴스 요약 실패: {}", response.getTitle());
                }
            } else {
                log.info(response.toString());
                log.warn("GPT 서비스에서 요약을 생성하지 못했습니다: {}", response.getTitle());
            }
        }
        return processedNews;
    }

    private String formatNewsItem(BingSearchResponse result) {
        return String.format("제목: %s\n내용: %s", result.getTitle(), result.getContent());
    }

    private News convertToNews(BingSearchResponse response, String summary, String tag) {
        return News.builder()
            .title(cleanText(response.getTitle()))
            .newsUrl(response.getNewsUrl())
            .content(cleanText(response.getContent()))
            .aiContent(cleanAndTruncate(summary, 200))
            .publishedAt(response.getPublishedAt())
            .thumbnailUrl(response.getThumbnailUrl())
            .tag(cleanAndTruncate(tag, 30))
            .build();
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.replaceAll("[#-]", "").trim();
    }

    private String cleanAndTruncate(String text, int maxLength) {
        if (text == null || text.isEmpty()) return "정보 없음";
        String cleaned = cleanText(text);
        return cleaned.length() > maxLength ? cleaned.substring(0, maxLength) : cleaned;
    }
}
