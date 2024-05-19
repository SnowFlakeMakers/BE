package com.snowflakes.rednose.service;


import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.stamp.ShowStampSpecificResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampRecord;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.StampRecordRepository;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.StampFixture;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class StampServiceTest {

    @Mock
    private StampRepository stampRepository;
    @Mock
    private StampLikeRepository stampLikeRepository;
    @Mock
    private StampRecordRepository stampRecordRepository;
    @InjectMocks
    private StampService stampService;

    @DisplayName("알맞은 우표 자세히보기 응답을 만들 수 있다")
    @Test
    void 우표_자세히보기() {
        // given
        final int LIKES = 10;
        final long STAMP_ID = 1L;
        final long MEMBER_ID = 1L;
        final Stamp STAMP = StampFixture.builder().numberOfLikes(LIKES).id(STAMP_ID).build();
        final Member JANG = MemberFixture.builder().id(MEMBER_ID).build();
        final Member GIL = MemberFixture.builder().build();
        final Member LEE = MemberFixture.builder().build();
        final boolean LIKED = true;

        final StampRecord JANG_RECORD = StampRecord.builder().stamp(STAMP).member(JANG).build();
        final StampRecord GIL_RECORD = StampRecord.builder().stamp(STAMP).member(GIL).build();
        final StampRecord LEE_RECORD = StampRecord.builder().stamp(STAMP).member(LEE).build();

        final List<StampRecord> COLLABORATORS = Arrays.asList(JANG_RECORD, GIL_RECORD, LEE_RECORD);

        when(stampRepository.findById(STAMP_ID)).thenReturn(Optional.of(STAMP));
        when(stampLikeRepository.existsByMemberIdAndStampId(MEMBER_ID, STAMP_ID)).thenReturn(LIKED);
        when(stampRecordRepository.findAllByStamp(STAMP)).thenReturn(
                COLLABORATORS);

        ShowStampSpecificResponse expected = ShowStampSpecificResponse.of(STAMP, LIKED,
                COLLABORATORS.stream().map(StampRecord::getMember).collect(
                        Collectors.toList()));

        // when
        ShowStampSpecificResponse actual = stampService.showSpecific(STAMP_ID, MEMBER_ID);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}