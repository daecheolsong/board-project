package com.example.boardproject.repository;

import com.example.boardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
