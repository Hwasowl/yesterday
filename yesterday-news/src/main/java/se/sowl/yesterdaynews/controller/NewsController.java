package se.sowl.yesterdaynews.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdaynews.service.NewsService;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/yesterday")
    public ResponseEntity<String> createNews() {
        newsService.saveYesterdayNews();
        return ResponseEntity.ok("OK");
    }

}
