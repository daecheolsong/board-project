package com.example.boardproject.service;

import com.example.boardproject.domain.Hashtag;
import com.example.boardproject.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비지니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService sut;
    @Mock
    private HashtagRepository hashtagRepository;

    @DisplayName("본문을 파싱하면, 해시태그 이름들을 중복 없이 반환한다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
    void givenContent_whenParsing_thenReturnsUniqueHashtagNames(String input, Set<String> expected) {


        Set<String> actual = sut.parseHashtagNames(input);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments> givenContent_whenParsing_thenReturnsUniqueHashtagNames() {
        return Stream.of(
                arguments(null, Set.of()),
                arguments("#java", Set.of("java")),
                arguments("j#ava", Set.of("ava")),
                arguments("#java-spring", Set.of("java")),
                arguments("#__java_spring", Set.of("__java_spring")),
                arguments("#java_spring", Set.of("java_spring")),
                arguments("#java#spring", Set.of("java", "spring")),
                arguments("#java   #spring", Set.of("java", "spring")),
                arguments("#java  #spring", Set.of("java", "spring")),
                arguments("   #java  #spring", Set.of("java", "spring")),
                arguments("   #java  #spring    ", Set.of("java", "spring")),
                arguments("#java#spring#boot", Set.of("java", "spring", "boot")),
                arguments("#java #spring#boot", Set.of("java", "spring", "boot")),
                arguments("#java #spring   #boot", Set.of("java", "spring", "boot")),
                arguments("#java#spring  ............", Set.of("java", "spring")),
                arguments("#spring ................#java", Set.of("java", "spring")),
                arguments("#", Set.of()),
                arguments("#   ", Set.of()),
                arguments("   #", Set.of()),
                arguments("", Set.of()),
                arguments(" ", Set.of())
        );
    }

    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭하는 것들을 중복 없이 반환한다.")
    @Test
    void givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet() {
        Set<String> hashtagNames = Set.of("java", "spring", "boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames))
                .willReturn(List.of(
                        Hashtag.of("java"),
                        Hashtag.of("spring")
                ));

        Set<Hashtag> hashtags = sut.findHashtagsByNames(hashtagNames);

        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }
}