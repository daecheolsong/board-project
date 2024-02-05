package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.ArticleComment;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.repository.ArticleCommentRepository;
import com.example.boardproject.repository.ArticleRepository;
import com.example.boardproject.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();
    }

    public void saveArticleComment(ArticleCommentDto articleCommentDto) {
        try {
            Article article = articleRepository.getReferenceById(articleCommentDto.articleId());
            UserAccount userAccount = userAccountRepository.findByUserId(articleCommentDto.userAccountDto().userId())
                    .orElseThrow(EntityNotFoundException::new);

            ArticleComment articleComment = articleCommentDto.toEntity(article, userAccount);

            if (articleCommentDto.parentCommentId() != null) {
                ArticleComment parentComment = articleCommentRepository.getReferenceById(articleCommentDto.parentCommentId());
                parentComment.addChildComment(articleComment);
            } else {
                articleCommentRepository.save(articleComment);
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글의 게시글을 찾을 수 없습니다 - dto: {}", articleCommentDto);
        }
    }

    public void updateArticleComment(ArticleCommentDto articleCommentDto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(articleCommentDto.id());
            if (articleCommentDto.content() != null) { articleComment.setContent(articleCommentDto.content()); }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", articleCommentDto);
        }
    }

    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }
}
