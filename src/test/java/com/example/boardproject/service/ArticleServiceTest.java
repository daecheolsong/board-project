package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleUpdateDto;
import com.example.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;
    @Mock
    private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList() {

        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");

        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {

        ArticleDto article = sut.searchArticle(1L);

        assertThat(article).isNotNull();
    }

    @DisplayName("게시글을 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {

        given(articleRepository.save(any(Article.class))).willReturn(null);

        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "song", "title1", "content", "#hashtag"));

        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID 와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {

        given(articleRepository.save(any(Article.class))).willReturn(null);

        sut.updateArticle(1L, ArticleUpdateDto.of("updateTitle", "updateContent", "#updateHashtag"));

        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID 를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {

        willDoNothing().given(articleRepository).delete(any(Article.class));

        sut.deleteArticle(1L);

        then(articleRepository).should().delete(any(Article.class));
    }

}