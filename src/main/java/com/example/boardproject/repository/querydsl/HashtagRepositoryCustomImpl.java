package com.example.boardproject.repository.querydsl;

import com.example.boardproject.domain.Hashtag;
import com.example.boardproject.domain.QHashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport
        implements HashtagRepositoryCustom {

    public HashtagRepositoryCustomImpl() {
        super(Hashtag.class);
    }

    @Override
    public List<String> findAllHashtagNames() {
        QHashtag hashtag = QHashtag.hashtag;

        return from(hashtag)
                .select(hashtag.hashtagName)
                .fetch();
    }
}
