package com.example.boardproject.repository;

import com.example.boardproject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author daecheol song
 * @since 1.0
 */
@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
