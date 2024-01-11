package com.example.boardproject.repository;

import com.example.boardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author daecheol song
 * @since 1.0
 */
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
