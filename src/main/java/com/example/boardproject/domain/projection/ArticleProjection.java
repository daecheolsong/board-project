package com.example.boardproject.domain.projection;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.UserAccount;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Projection(name = "withUserAccount", types = Article.class)
public interface ArticleProjection {
    Long getId();
    UserAccount getUserAccount();
    String getTitle();
    String getContent();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
