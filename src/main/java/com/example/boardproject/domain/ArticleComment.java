package com.example.boardproject.domain;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
public class ArticleComment {
    private Long id;
    private Article article;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
