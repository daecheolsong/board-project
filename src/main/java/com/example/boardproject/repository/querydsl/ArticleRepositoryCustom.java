package com.example.boardproject.repository.querydsl;

import com.example.boardproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface ArticleRepositoryCustom {
    /**
     * @deprecated 해시태그 도메인을 새로 만들었으므로 이 코드는 더이상 사용X
     * @see HashtagRepositoryCustom#findAllHashtagNames()
     */
    @Deprecated
    List<String> findAllDistinctHashtags();

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
