package se.sowl.yesterdayai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdayai.dto.BingSearchResponse;
import se.sowl.yesterdayai.service.BingSearchService;
import se.sowl.yesterdayai.service.OpenAiService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final BingSearchService newsService;
//    private final OpenAiService openAiService;

    @GetMapping
    public void createNews() {
        List<BingSearchResponse> yesterdayNews = newsService.getYesterdayNews();
        System.out.println(yesterdayNews);
//        openAiService.summarizeNews(yesterdayNews.get(0).get("description"));
        // database save
    }
}
