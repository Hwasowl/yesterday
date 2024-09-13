package se.sowl.yesterdaynews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaynews.dto.BingSearchResponse;
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

    public void saveYesterdayNews() {
        List<BingSearchResponse> searchResults = bingSearchService.getYesterdayNews();
        List<News> processedNews = processSearchResults(searchResults);
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
