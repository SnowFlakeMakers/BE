package com.snowflakes.rednose.repository.stamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampRecord;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampRecordRepository;
import com.snowflakes.rednose.support.RepositoryTest;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.StampFixture;
import java.time.LocalDateTime;
import java.util.List;
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StampRecordRepository stampRecordRepository;


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
        final Stamp BIRTHDAY_STAMP = 저장(StampFixture.builder().createdAt(LocalDateTime.now().minusDays(2))
                .build());
        final Stamp CHRISTMAS_STAMP = 저장(StampFixture.builder().createdAt(LocalDateTime.now()).build());
        final Stamp ANNIVERSARY_STAMP = 저장(StampFixture.builder().createdAt(LocalDateTime.now().minusDays(1))
                .build());

        final Member JANG = 저장(MemberFixture.builder().nickname("장지담").build());

        저장(StampRecord.builder().member(JANG).stamp(BIRTHDAY_STAMP).build());
        저장(StampRecord.builder().member(JANG).stamp(CHRISTMAS_STAMP).build());
        저장(StampRecord.builder().member(JANG).stamp(ANNIVERSARY_STAMP).build());

        // when
        Page<Stamp> page0 = stampRepository.findAllAtBoard(null,
                PageRequest.of(0, 2, Sort.by("createdAt").descending()));
        Page<Stamp> page1 = stampRepository.findAllAtBoard(null,
                PageRequest.of(1, 2, Sort.by("createdAt").descending()));

        // then
        assertAll(
                () -> assertThat(page0.getContent()).containsExactly(CHRISTMAS_STAMP, ANNIVERSARY_STAMP),
                () -> assertThat(page1.getContent()).containsExactly(BIRTHDAY_STAMP)
        );
    }

    @DisplayName("우표 목록을 좋아요 순으로 페이지에 맞게 조회할 수 있다")
    @Test
    void 우표_목록_좋아요순_조회() {
        // given
        final Stamp BIRTHDAY_STAMP = 저장(StampFixture.builder().numberOfLikes(3)
                .build());
        final Stamp CHRISTMAS_STAMP = 저장(StampFixture.builder().numberOfLikes(1).build());
        final Stamp ANNIVERSARY_STAMP = 저장(StampFixture.builder().numberOfLikes(2)
                .build());

        final Member JANG = 저장(MemberFixture.builder().nickname("장지담").build());

        저장(StampRecord.builder().member(JANG).stamp(BIRTHDAY_STAMP).build());
        저장(StampRecord.builder().member(JANG).stamp(CHRISTMAS_STAMP).build());
        저장(StampRecord.builder().member(JANG).stamp(ANNIVERSARY_STAMP).build());

        // when
        Slice<Stamp> slice0 = stampRepository.findAllAtBoard(null,
                PageRequest.of(0, 2, Sort.by("numberOfLikes").descending()));
        Slice<Stamp> slice1 = stampRepository.findAllAtBoard(null,
                PageRequest.of(1, 2, Sort.by("numberOfLikes").descending()));

        // then
        assertAll(
                () -> assertThat(slice0.getContent()).containsExactly(BIRTHDAY_STAMP, ANNIVERSARY_STAMP),
                () -> assertThat(slice1.getContent()).containsExactly(CHRISTMAS_STAMP)
        );
    }

    private Stamp 저장(Stamp stamp3) {
        return stampRepository.save(stamp3);
    }

    @Test
    void memberId로_내가_만든_우표를_조회할_수_있다() {
        Stamp stamp1 = stampRepository.save(StampFixture.builder().build());
        Stamp stamp2 = stampRepository.save(StampFixture.builder().build());

        Member member1 = memberRepository.save(MemberFixture.builder().build());
        Member member2 = memberRepository.save(MemberFixture.builder().build());

        stampRecordRepository.save(StampRecord.builder().member(member1).stamp(stamp1).build());
        stampRecordRepository.save(StampRecord.builder().member(member1).stamp(stamp2).build());
        stampRecordRepository.save(StampRecord.builder().member(member2).stamp(stamp2).build());

        PageRequest pageRequest = PageRequest.of(0, 2);
        Slice<Stamp> stamps1 = stampRepository.findMyStampsByMemberId(member1.getId(), pageRequest);
        Slice<Stamp> stamps2 = stampRepository.findMyStampsByMemberId(member2.getId(), pageRequest);

        assertAll(
                () -> assertThat(stamps1.getContent()).containsExactly(stamp1, stamp2),
                () -> assertThat(stamps2.getContent()).containsExactly(stamp2)
        );

    }


    @DisplayName("우표게시판에서 검색할 수 있다")
    @Test
    void 우표게시판_검색가능() {
        // given
        final Member JANG = 저장(MemberFixture.builder().nickname("장지담").build());
        final Member GIL = 저장(MemberFixture.builder().nickname("길가은").build());
        final Member LEE = 저장(MemberFixture.builder().nickname("이지민").build());

        final Stamp BIRTHDAY_STAMP = 저장(
                StampFixture.builder().name("HBD 생일축하 우표").createdAt(LocalDateTime.now().minusDays(2))
                        .build());
        final Stamp CHRISTMAS_STAMP = 저장(
                StampFixture.builder().name("외로운 크리스마스 우표").createdAt(LocalDateTime.now()).build());
        final Stamp ANNIVERSARY_STAMP = 저장(
                StampFixture.builder().name("우리 벌써 100일 우표").createdAt(LocalDateTime.now().minusDays(1))
                        .build());
        final Stamp JANG_STAMP = 저장(
                StampFixture.builder().name("우표 이름이 장지담").createdAt(LocalDateTime.now().minusDays(1))
                        .build());

        저장(StampRecord.builder().member(GIL).stamp(JANG_STAMP).build());

        저장(StampRecord.builder().member(JANG).stamp(BIRTHDAY_STAMP).build());
        저장(StampRecord.builder().member(GIL).stamp(BIRTHDAY_STAMP).build());

        저장(StampRecord.builder().member(JANG).stamp(CHRISTMAS_STAMP).build());

        저장(StampRecord.builder().member(GIL).stamp(ANNIVERSARY_STAMP).build());
        저장(StampRecord.builder().member(LEE).stamp(ANNIVERSARY_STAMP).build());

        final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

        // when
        final List<Stamp> JANG_KEYWORD = stampRepository.findAllAtBoard(JANG.getNickname(), PAGE_REQUEST).getContent();
        final List<Stamp> GIL_KEYWORD = stampRepository.findAllAtBoard(GIL.getNickname(), PAGE_REQUEST).getContent();
        final List<Stamp> LEE_KEYWORD = stampRepository.findAllAtBoard(LEE.getNickname(), PAGE_REQUEST).getContent();

        // then
        assertAll(
                () -> assertThat(JANG_KEYWORD).contains(JANG_STAMP, BIRTHDAY_STAMP, CHRISTMAS_STAMP),
                () -> assertThat(JANG_KEYWORD.size()).isEqualTo(3),
                () -> assertThat(GIL_KEYWORD).contains(JANG_STAMP, BIRTHDAY_STAMP, ANNIVERSARY_STAMP),
                () -> assertThat(GIL_KEYWORD.size()).isEqualTo(3),
                () -> assertThat(LEE_KEYWORD).contains(ANNIVERSARY_STAMP),
                () -> assertThat(LEE_KEYWORD.size()).isEqualTo(1)
        );
    }

    private Member 저장(Member member) {
        return memberRepository.save(member);
    }

    private StampRecord 저장(StampRecord stampRecord) {
        return stampRecordRepository.save(stampRecord);
    }


}