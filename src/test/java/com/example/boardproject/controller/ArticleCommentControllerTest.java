package com.example.boardproject.controller;


import com.example.boardproject.config.TestSecurityConfig;
import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.dto.request.ArticleCommentRequest;
import com.example.boardproject.service.ArticleCommentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

/**
 * @author daecheol song
 * @since 1.0
 */
@DisplayName("View 컨트롤러 - 댓글")
@Import({TestSecurityConfig.class})
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    private final MockMvc mvc;

    @MockBean
    private ArticleCommentService articleCommentService;


    public ArticleCommentControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @WithUserDetails(value = "tester1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
    @Test
    void givenArticleCommentInfo_whenRequesting_thenSavesNewArticleComment() throws Exception {

        long articleId = 1L;
        ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "test comment");
        willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));

        mvc.perform(
                        post("/comments/new")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(encode(request))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
    }

    @WithUserDetails(value = "tester1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 댓글 삭제 - 정상 호출")
    @Test
    void givenArticleCommentIdToDelete_whenRequesting_thenDeletesArticleComment() throws Exception {

        long articleId = 1L;
        long articleCommentId = 1L;
        String userId = "tester1";
        willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId, userId);

        mvc.perform(
                        post("/comments/" + articleCommentId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(encode(Map.of("articleId", articleId)))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().deleteArticleComment(articleCommentId, userId);
    }

    private String encode(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> fieldMap = mapper.convertValue(obj, new TypeReference<>() {});
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.setAll(fieldMap);

        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }

}
