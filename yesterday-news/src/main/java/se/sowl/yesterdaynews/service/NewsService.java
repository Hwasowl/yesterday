package se.sowl.yesterdaynews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaydomain.news.repository.NewsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<News> getAllNews() {
        // TODO: 일자로 요청 & Pageable
        return newsRepository.findAll();
    }

    public News saveNews(News news) {
        // TODO: GPT 호출 구현
        // String summary = gptApiClient.summarize(news.getContent());
        // news.setSummary(summary);

        News savedNews = newsRepository.save(news);
        kafkaTemplate.send("news-topic", savedNews.getId().toString());
        return savedNews;
    }
}
