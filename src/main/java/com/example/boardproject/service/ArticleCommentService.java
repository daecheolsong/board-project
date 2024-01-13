package com.example.boardproject.service;

import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDto articleCommentDto) {

    }

    public void updateArticleComment(ArticleCommentDto articleCommentUpdateDto) {
    }

    public void deleteArticleComment(Long articleCommentId) {

    }
}
