package se.sowl.yesterdaydomain.news.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "news_url")
    private String newsUrl;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    private String tag;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Builder
    public News(String title, String content, String thumbnailUrl, String newsUrl, LocalDateTime publishedAt, String tag) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.newsUrl = newsUrl;
        this.publishedAt = publishedAt;
        this.tag = tag;
    }
}
