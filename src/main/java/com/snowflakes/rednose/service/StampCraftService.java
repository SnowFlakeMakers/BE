package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.EnterStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.PaintStampRequest;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.StampCraft;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.snowflakes.rednose.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.snowflakes.rednose.exception.ErrorCode.STAMP_CRAFT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StampCraftService {

    private final MemberRepository memberRepository;
    private Long ID = 0L;
    private Map<Long, StampCraft> stampCrafts = new ConcurrentHashMap<>();

    @Transactional
    public CreateStampCraftResponse create(CreateStampCraftRequest request, Long memberId) {
        Member member = findMemberById(memberId);
        StampCraft stampCraft = StampCraft.builder().host(member).canvasType(request.getCanvasType()).build();
        stampCrafts.put(ID++, stampCraft);
        return CreateStampCraftResponse.from(ID);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public void paint(Long stampCraftId, PaintStampRequest request) {
        validExistStampCraft(stampCraftId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        stampCraft.paint(request.getX(), request.getY(), request.getColor());
        stampCrafts.put(stampCraftId, stampCraft);
    }

    private void validExistStampCraft(Long stampCraftId) {
        if (!stampCrafts.containsKey(stampCraftId)) {
            throw new NotFoundException(STAMP_CRAFT_NOT_FOUND);
        }
    }

    public EnterStampCraftResponse enter(Long stampCraftId, Long memberId) {
        Member member = findMemberById(memberId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        stampCraft.enter(member);
        return EnterStampCraftResponse.from(member.getNickname());
    }
}
