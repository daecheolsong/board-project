package com.example.boardproject.dto;

/**
 * @author daecheol song
 * @since 1.0
 */
public record ArticleUpdateDto(
        String title,
        String content,
        String hashtag
) {

    public static ArticleUpdateDto of(String title, String content, String hashtag) {
        return new ArticleUpdateDto(title, content, hashtag);
    }
}
