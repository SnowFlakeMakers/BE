package com.snowflakes.rednose.repository.stamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.support.RepositoryTest;
import com.snowflakes.rednose.support.fixture.StampFixture;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;


@Slf4j
@RepositoryTest
class StampRepositoryTest {

    @Autowired
    private StampRepository stampRepository;

    @MockBean
    DateTimeProvider dateTimeProvider;

    @SpyBean
    AuditingHandler auditingHandler;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @DisplayName("우표 목록을 최신순으로 페이지에 맞게 조회할 수 있다")
    @Test
    void 우표_목록_최신순_조회() {
        // given
        Stamp stamp1 = StampFixture.builder().createdAt(LocalDateTime.now()).build();
        Stamp stamp2 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(1))
                .build();
        Stamp stamp3 = StampFixture.builder().createdAt(LocalDateTime.now().minusDays(2))
                .build();

        stampRepository.save(stamp3);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);

        // when
        Page<Stamp> page0 = stampRepository.findAll(PageRequest.of(0, 2, Sort.by("createdAt").descending()));
        Page<Stamp> page1 = stampRepository.findAll(PageRequest.of(1, 2, Sort.by("createdAt").descending()));

        // then
        assertAll(
                () -> assertThat(page0.getContent()).containsExactly(stamp1, stamp2),
                () -> assertThat(page1.getContent()).containsExactly(stamp3)
        );
    }

    @DisplayName("우표 목록을 좋아요 순으로 페이지에 맞게 조회할 수 있다")
    @Test
    void 우표_목록_좋아요순_조회() {
        // given
        Stamp stamp1 = StampFixture.builder().numberOfLikes(1).build();
        Stamp stamp2 = StampFixture.builder().numberOfLikes(2)
                .build();
        Stamp stamp3 = StampFixture.builder().numberOfLikes(3)
                .build();

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