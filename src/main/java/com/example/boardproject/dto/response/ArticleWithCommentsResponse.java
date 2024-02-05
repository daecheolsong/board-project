package com.example.boardproject.dto.response;

import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.dto.ArticleWithCommentsDto;
import com.example.boardproject.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author daecheol song
 * @since 1.0
 */
public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname,
        Set<ArticleCommentsResponse> articleCommentResponses,
        String userId
) {

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, Set<ArticleCommentsResponse> articleCommentResponses, String userId) {
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, articleCommentResponses, userId);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                organizeChildComments(dto.articleCommentDtos()),
                dto.userAccountDto().userId()
        );
    }

    private static Set<ArticleCommentsResponse> organizeChildComments(Set<ArticleCommentDto> dtos) {
        Map<Long, ArticleCommentsResponse> map = dtos.stream()
                .map(ArticleCommentsResponse::from)
                .collect(Collectors.toMap(ArticleCommentsResponse::id, Function.identity()));

        map.values().stream()
                .filter(ArticleCommentsResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentsResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(ArticleCommentsResponse::createdAt)
                                .reversed()
                                .thenComparingLong(ArticleCommentsResponse::id)
                        )
                ));
    }

}

