package se.sowl.yesterdaynews.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaynews.service.news.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    @GetMapping("/yesterday")
    public ResponseEntity<List<News>> getYesterdayNews() {
        return ResponseEntity.ok(newsService.getYesterdayNews());
    }

    @PostMapping("/yesterday")
    public ResponseEntity<String> createNews() {
        return ResponseEntity.ok("OK");
    }
}
