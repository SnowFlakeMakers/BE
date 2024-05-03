package com.snowflakes.rednose.service;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampLike;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.snowflakes.rednose.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampLikeService {

    private final StampLikeRepository stampLikeRepository;
    private final StampRepository stampRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void like(Long stampId, Long memberId) {
        Stamp stamp = stampRepository.findById(stampId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
        Member member= memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
        StampLike like = StampLike.builder().stamp(stamp).member(member).build();
        stampLikeRepository.save(like);
    }

    public ShowStampLikeResponse getLikes(Long memberId) {
        Slice<Stamp> stamps = stampRepository.findLikesByMemberId(memberId);
        return ShowStampLikeResponse.from(stamps);
    }
}
