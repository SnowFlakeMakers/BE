package com.snowflakes.rednose.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.seal.ShowSealSpecificResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.SealFixture;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SealServiceTest {

    @Mock
    private SealRepository sealRepository;
    @Mock
    private SealLikeRepository sealLikeRepository;

    @InjectMocks
    private SealService sealService;

    @DisplayName("알맞은 우표 자세히보기 응답을 얻을 수 있다")
    @Test
    void 우표_자세히보기() {
        // given
        final Long MEMBER_ID = 1L;
        final Member MEMBER = MemberFixture.builder().id(MEMBER_ID).build();

        final Long SEAL_ID = 1L;
        final int LIKES = 10;
        final Seal SEAL = SealFixture.builder().member(MEMBER).build();

        final boolean LIKED = true;

        when(sealRepository.findById(SEAL_ID)).thenReturn(Optional.of(SEAL));
        when(sealLikeRepository.existsByMemberIdAndSealId(MEMBER_ID, SEAL_ID)).thenReturn(LIKED);

        final ShowSealSpecificResponse EXPECTED = ShowSealSpecificResponse.of(SEAL, LIKED);

        // when
        final ShowSealSpecificResponse ACTUAL = sealService.showSpecific(SEAL_ID, MEMBER_ID);

        // then
        assertThat(ACTUAL).usingRecursiveComparison().isEqualTo(EXPECTED);
    }
}