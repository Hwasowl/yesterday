package se.sowl.yesterdayai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.yesterdayai.dto.BingSearchResponse;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaydomain.news.repository.NewsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final BingSearchService bingSearchService;
    private final GPTService gptService;
    private final NewsRepository newsRepository;

    public void processAndSaveNews() {
        List<BingSearchResponse> searchResults = bingSearchService.getYesterdayNews();
        List<News> processedNews = processSearchResults(searchResults);
        System.out.println("Processed news: " + processedNews);
        saveArticles(processedNews);
    }

    private List<News> processSearchResults(List<BingSearchResponse> searchResults) {
        return searchResults.stream()
            .map(this::convertToNews)
            .collect(Collectors.toList());
    }

    private News convertToNews(BingSearchResponse response) {
        String summary = gptService.summarizeText(response.getContent());
        return News.builder()
            .title(response.getTitle())
            .newsUrl(response.getNewsUrl())
            .content(summary)
            .publishedAt(response.getPublishedAt())
            .thumbnailUrl(response.getThumbnailUrl())
            .build();
    }

    private void saveArticles(List<News> articles) {
        newsRepository.saveAll(articles);
    }
}
