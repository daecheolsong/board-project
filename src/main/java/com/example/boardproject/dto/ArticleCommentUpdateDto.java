package com.example.boardproject.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.boardproject.domain.ArticleComment}
 */
public record ArticleCommentUpdateDto(
        String content
) {

    public static ArticleCommentUpdateDto of (String content) {
        return new ArticleCommentUpdateDto(content);
    }
}