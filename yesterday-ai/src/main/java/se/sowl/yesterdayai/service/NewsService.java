package se.sowl.yesterdayai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaydomain.news.repository.NewsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final BingSearchService bingSearchService;

    private final OpenAiService gptService;

    private final NewsRepository newsRepository;
}
