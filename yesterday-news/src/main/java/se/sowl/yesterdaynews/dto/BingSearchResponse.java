package se.sowl.yesterdaynews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@AllArgsConstructor
@Getter
public class BingSearchResponse {
    private static final String HIGH_QUALITY_SUFFIX = "&w=500&h=300&c=7";

    private String title;
    private String newsUrl;
    private String content;
    private LocalDateTime publishedAt;
    private String thumbnailUrl;

    @SuppressWarnings("unchecked")
    public BingSearchResponse(Map<String, Object> result) {
        this.title = (String) result.get("name");
        this.newsUrl = (String) result.get("url");
        this.content = (String) result.get("description");
        this.publishedAt = parseDateTime((String) result.get("datePublished"));

        Map<String, Object> image = (Map<String, Object>) result.get("image");
        if (image != null) {
            Map<String, Object> thumbnail = (Map<String, Object>) image.get("thumbnail");
            if (thumbnail != null) {
                String url = (String) thumbnail.get("contentUrl");
                this.thumbnailUrl = url != null ? url + HIGH_QUALITY_SUFFIX : null;
            }
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse date: " + dateTimeStr + ". Using current time instead.");
            return LocalDateTime.now();
        }
    }
}
