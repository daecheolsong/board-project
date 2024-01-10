package com.example.boardproject.repository;

import com.example.boardproject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
