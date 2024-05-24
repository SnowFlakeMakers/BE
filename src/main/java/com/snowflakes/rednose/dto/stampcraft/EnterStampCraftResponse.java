package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EnterStampCraftResponse {

    private MessageType type;
    private String nickname;

    @Builder
    public EnterStampCraftResponse(MessageType type, String nickname) {
        this.type = type;
        this.nickname = nickname;
    }

    public static EnterStampCraftResponse from(Member member) {
        return EnterStampCraftResponse.builder()
                .type(MessageType.ENTER)
                .nickname(member.getNickname())
                .build();
    }

}
