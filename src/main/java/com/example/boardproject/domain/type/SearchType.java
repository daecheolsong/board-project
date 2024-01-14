package com.example.boardproject.domain.type;

import lombok.Getter;

/**
 * @author daecheol song
 * @since 1.0
 */
public enum SearchType {

    TITLE("제목"),
    CONTENT("본문"),
    ID("유저 ID"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그"),
    NONE("선택 안함");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public static SearchType of(String value) {
        for(SearchType v : values())
            if(v.getDescription().equalsIgnoreCase(value)) return v;
        return SearchType.NONE;
    }
}
