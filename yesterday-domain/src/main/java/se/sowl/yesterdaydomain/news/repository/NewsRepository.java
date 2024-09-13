package se.sowl.yesterdaydomain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.yesterdaydomain.news.domain.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
