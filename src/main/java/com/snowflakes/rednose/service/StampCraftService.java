package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.StampCraft;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampCraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.snowflakes.rednose.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StampCraftService {

    private MemberRepository memberRepository;
    private StampCraftRepository stampCraftRepository;

    @Transactional
    public CreateStampCraftResponse create(CreateStampCraftRequest request, Long memberId) {
        Member member = findMemberById(memberId);
        StampCraft stampCraft = StampCraft.builder().host(member).canvasType(request.getCanvasType()).build();
        stampCraftRepository.save(stampCraft);
        return CreateStampCraftResponse.from(stampCraft.getId());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }
}
