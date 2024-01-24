package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.Hashtag;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleWithCommentsDto;
import com.example.boardproject.dto.HashtagDto;
import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.repository.ArticleRepository;
import com.example.boardproject.repository.HashtagRepository;
import com.example.boardproject.repository.UserAccountRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private HashtagService hashtagService;

    @Mock
    private HashtagRepository hashtagRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {

        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {

        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {

        Pageable pageable = Pageable.ofSize(20);

        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(hashtagRepository).shouldHaveNoInteractions();
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage() {

        String hashtagName = "java";
        Pageable pageable = Pageable.ofSize(20);
        Article expectedArticle = createArticle();
        given(articleRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(expectedArticle), pageable, 1));

        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName, pageable);

        assertThat(articles).isEqualTo(new PageImpl<>(List.of(ArticleDto.from(expectedArticle)), pageable, 1));
        then(articleRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달린 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComments() {

        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtagDtos", article.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()));
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticleWithComments_thenThrowsException() {

        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        Throwable t = catchThrowable(() -> sut.getArticleWithComments(articleId));

        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {

        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleDto dto = sut.getArticle(articleId);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtagDtos", article.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()));
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {

        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        Throwable t = catchThrowable(() -> sut.getArticle(articleId));

        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNonexistentHashtag_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {

        String hashtagName = "XXXXX";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(), pageable, 0));

        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName, pageable);

        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
    }

    @DisplayName("게시글 정보를 입력하면, 본문에서 해시태그 정보를 추출하여 해시태그 정보가 포함된 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenExtractHashtagsFromContentAndSavesArticleWithExtractedHashtags() {

        ArticleDto dto = createArticleDto();
        given(userAccountRepository.findByUserId(dto.userAccountDto().userId())).willReturn(Optional.of(createUserAccount()));
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());

        sut.saveArticle(dto);

        then(userAccountRepository).should().findByUserId(dto.userAccountDto().userId());
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {

        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용 #springboot");
        Set<String> expectedHashtagNames = Set.of("springboot");
        Set<Hashtag> expectedHashtags = new HashSet<>();

        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
        given(userAccountRepository.findByUserId(dto.userAccountDto().userId())).willReturn(Optional.of(dto.userAccountDto().toEntity()));
        willDoNothing().given(articleRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutArticles(any());
        given(hashtagService.parseHashtagNames(dto.content())).willReturn(expectedHashtagNames);
        given(hashtagService.findHashtagsByNames(expectedHashtagNames)).willReturn(expectedHashtags);

        sut.updateArticle(dto.id(), dto);

        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .extracting("hashtags", as(InstanceOfAssertFactories.COLLECTION))
                .hasSize(1)
                .extracting("hashtagName")
                .containsExactly("springboot");
        then(articleRepository).should().getReferenceById(dto.id());
        then(userAccountRepository).should().findByUserId(dto.userAccountDto().userId());
        then(articleRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutArticles(any());
        then(hashtagService).should().parseHashtagNames(dto.content());
        then(hashtagService).should().findHashtagsByNames(expectedHashtagNames);
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {

        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        sut.updateArticle(dto.id(), dto);

        then(articleRepository).should().getReferenceById(dto.id());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(hashtagService).shouldHaveNoInteractions();
    }

    @DisplayName("게시글 작성자가 아닌 사람이 수정 정보를 입력하면, 아무 것도 하지 않는다.")
    @Test
    void givenModifiedArticleInfoWithDifferentUser_whenUpdatingArticle_thenDoesNothing() {
        // Given
        Long differentArticleId = 22L;
        Article differentArticle = createArticle(differentArticleId);
        differentArticle.setUserAccount(createUserAccount("John"));
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");
        given(articleRepository.getReferenceById(differentArticleId)).willReturn(differentArticle);
        given(userAccountRepository.findByUserId(dto.userAccountDto().userId())).willReturn(Optional.of(dto.userAccountDto().toEntity()));

        // When
        sut.updateArticle(differentArticleId, dto);

        // Then
        then(articleRepository).should().getReferenceById(differentArticleId);
        then(userAccountRepository).should().findByUserId(dto.userAccountDto().userId());
        then(hashtagService).shouldHaveNoInteractions();
    }
    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {

        Long articleId = 1L;
        String userId = "song";

        given(articleRepository.getReferenceById(articleId)).willReturn(createArticle());
        willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId, userId);
        willDoNothing().given(articleRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutArticles(any());

        sut.deleteArticle(1L, userId);

        then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId, userId);
        then(articleRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutArticles(any());
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingArticles_thenReturnsArticleCount() {

        long expected = 0L;
        given(articleRepository.count()).willReturn(expected);

        long actual = sut.getArticleCount();

        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().count();
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {

        List<String> expectedHashtags = List.of("java", "spring", "boot");
        given(hashtagRepository.findAllHashtagNames()).willReturn(expectedHashtags);

        List<String> actualHashtags = sut.getHashtags();

        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(hashtagRepository).should().findAllHashtagNames();
    }


    private UserAccount createUserAccount() {
        return createUserAccount("song");
    }
    private UserAccount createUserAccount(String userId) {
        return UserAccount.of(
                userId,
                "password",
                "uno@email.com",
                "ssong",
                null
        );
    }

    private Article createArticle() {
        return createArticle(1L);
    }

    private Article createArticle(Long id) {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );

        article.addHashtags(Set.of(
                createHashtag(1L, "java"),
                createHashtag(2L, "spring")
        ));
        return article;
    }

    private Hashtag createHashtag(String hashtagName) {
        return createHashtag(1L, hashtagName);
    }

    private Hashtag createHashtag(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.of(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);

        return hashtag;
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content");
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "ssong",
                LocalDateTime.now(),
                "ssong");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "song",
                "password",
                "song@mail.com",
                "ssong",
                "This is memo",
                LocalDateTime.now(),
                "ssong",
                LocalDateTime.now(),
                "ssong"
        );
    }

}