package se.sowl.yesterdaynews.service.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaynews.dto.BingSearchResponse;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaydomain.news.repository.NewsRepository;
import se.sowl.yesterdaynews.service.BingSearchService;
import se.sowl.yesterdaynews.service.gpt.GPTResponseParserService;
import se.sowl.yesterdaynews.service.gpt.GPTService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final BingSearchService bingSearchService;
    private final GPTService gptService;
    private final GPTResponseParserService gptResponseParserService;
    private final NewsRepository newsRepository;
    private final NewsUtils newsUtils;

    public List<News> getYesterdayNews() {
        return newsRepository.findAll();
    }

    public void saveYesterdayNews() {
        List<BingSearchResponse> searchResults = bingSearchService.getYesterdayNews();
        log.info("{} 개의 뉴스를 조회했습니다.", searchResults.size());
        List<News> processedNews = processSearchResults(searchResults);
        log.info("{} 개의 뉴스를 저장했습니다.", processedNews.size());
        newsRepository.saveAll(processedNews);
    }

    private List<News> processSearchResults(List<BingSearchResponse> searchResults) {
        List<String> newsItems = convertToNewsItems(searchResults);
        List<String> summariesAndTags = gptService.summarizeAndTagNews(newsItems);
        List<String> parsedResults = gptResponseParserService.parseBatchResult(String.join("---", summariesAndTags));
        return createProcessedNewsList(searchResults, parsedResults);
    }

    private List<String> convertToNewsItems(List<BingSearchResponse> searchResults) {
        return searchResults.stream()
            .map(newsUtils::formatNewsItem)
            .collect(Collectors.toList());
    }

    private List<News> createProcessedNewsList(List<BingSearchResponse> searchResults, List<String> parsedResults) {
        List<News> processedNews = new ArrayList<>();
        convertSummaryToNews(searchResults, parsedResults, processedNews);
        return processedNews;
    }

    private void convertSummaryToNews(List<BingSearchResponse> searchResults, List<String> parsedResults, List<News> processedNews) {
        for (int i = 0; i < searchResults.size(); i++) {
            BingSearchResponse response = searchResults.get(i);
            if (i >= parsedResults.size()) {
                log.warn("뉴스 요약 실패: {}", response.getTitle());
                continue;
            }
            String[] summaryAndTag = newsUtils.extractSummaryAndTag(parsedResults.get(i));
            processedNews.add(newsUtils.convertToNews(response, summaryAndTag[0], summaryAndTag[1]));
        }
    }
}
