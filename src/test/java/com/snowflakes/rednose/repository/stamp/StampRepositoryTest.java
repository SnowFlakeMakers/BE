package com.snowflakes.rednose.repository.stamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.support.RepositoryTest;
import com.snowflakes.rednose.support.fixture.StampFixture;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;


@Slf4j
@RepositoryTest
class StampRepositoryTest {

    @Autowired
    private StampRepository stampRepository;


    @DisplayName("우표 목록을 최신순으로 페이지에 맞게 조회할 수 있다")
    @Test
    void 우표_목록_최신순_조회() {
        // given
        Stamp stamp1 = StampFixture.builder().createdAt(LocalDateTime.now()).numberOfLikes(1).build().toStamp();
        Stamp stamp2 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(1)).numberOfLikes(2)
                .build().toStamp();
        Stamp stamp3 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(2)).numberOfLikes(3)
                .build().toStamp();

        stampRepository.save(stamp3);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);

        // when
        Slice<Stamp> slice0 = stampRepository.findAll(PageRequest.of(0, 2, Sort.by("createdAt").descending()));
        Slice<Stamp> slice1 = stampRepository.findAll(PageRequest.of(1, 2, Sort.by("createdAt").descending()));

        // then
        assertAll(
                () -> assertThat(slice0.getContent()).containsExactly(stamp1, stamp2),
                () -> assertThat(slice1.getContent()).containsExactly(stamp3)
        );
    }

    @DisplayName("우표 목록을 좋아요 순으로 페이지에 맞게 조회할 수 있다")
    @Test
    void 우표_목록_좋아요순_조회() {
        // given
        Stamp stamp1 = StampFixture.builder().createdAt(LocalDateTime.now()).numberOfLikes(1).build().toStamp();
        Stamp stamp2 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(1)).numberOfLikes(2)
                .build().toStamp();
        Stamp stamp3 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(2)).numberOfLikes(3)
                .build().toStamp();

        stampRepository.save(stamp2);
        stampRepository.save(stamp3);
        stampRepository.save(stamp1);

        // when
        Slice<Stamp> slice0 = stampRepository.findAll(PageRequest.of(0, 2, Sort.by("numberOfLikes").descending()));
        Slice<Stamp> slice1 = stampRepository.findAll(PageRequest.of(1, 2, Sort.by("numberOfLikes").descending()));

        // then
        assertAll(
                () -> assertThat(slice0.getContent()).containsExactly(stamp3, stamp2),
                () -> assertThat(slice1.getContent()).containsExactly(stamp1)
        );
    }


}