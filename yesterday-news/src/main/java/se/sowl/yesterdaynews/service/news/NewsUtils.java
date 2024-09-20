package se.sowl.yesterdaynews.service.news;

import org.springframework.stereotype.Component;
import se.sowl.yesterdaydomain.news.domain.News;
import se.sowl.yesterdaynews.dto.BingSearchResponse;

@Component
public class NewsUtils {
    public String formatNewsItem(BingSearchResponse result) {
        return String.format("제목: %s\n내용: %s", result.getTitle(), result.getContent());
    }

    public News convertToNews(BingSearchResponse response, String summary, String tag) {
        return News.builder()
            .title(cleanText(response.getTitle()))
            .newsUrl(response.getNewsUrl())
            .content(cleanText(response.getContent()))
            .aiContent(cleanAndTruncate(summary, 200))
            .publishedAt(response.getPublishedAt())
            .thumbnailUrl(response.getThumbnailUrl())
            .tag(cleanAndTruncate(tag, 30))
            .build();
    }

    public String[] extractSummaryAndTag(String parsedResult) {
        String summary = parsedResult.substring(parsedResult.indexOf("요약:") + 3, parsedResult.indexOf("태그:")).trim();
        String tag = parsedResult.substring(parsedResult.indexOf("태그:") + 3).trim();
        return new String[]{summary, tag};
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.replaceAll("[#-]", "").trim();
    }

    private String cleanAndTruncate(String text, int maxLength) {
        if (text == null || text.isEmpty()) return "정보 없음";
        String cleaned = cleanText(text);
        return cleaned.length() > maxLength ? cleaned.substring(0, maxLength) : cleaned;
    }
}
