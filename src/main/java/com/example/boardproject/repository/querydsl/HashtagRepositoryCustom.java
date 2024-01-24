package com.example.boardproject.repository.querydsl;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface HashtagRepositoryCustom {
    List<String> findAllHashtagNames();
}
