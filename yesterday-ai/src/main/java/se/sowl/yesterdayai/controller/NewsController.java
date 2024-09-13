package se.sowl.yesterdayai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdayai.service.NewsService;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public void createNews() {
        newsService.processAndSaveNews();
    }
}
