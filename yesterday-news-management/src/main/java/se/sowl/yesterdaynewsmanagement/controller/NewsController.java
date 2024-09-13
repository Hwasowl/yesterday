package se.sowl.yesterdaynewsmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaynewsmanagement.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        return newsService.saveNews(news);
    }
}
