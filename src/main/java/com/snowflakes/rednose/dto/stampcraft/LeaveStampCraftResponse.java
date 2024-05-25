package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.StampCraft;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LeaveStampCraftResponse {

    private MessageType type;
    private String nickname;
    private String host;

    @Builder
    public LeaveStampCraftResponse(MessageType type, String nickname, String host) {
        this.type = type;
        this.nickname = nickname;
        this.host = host;
    }

    public static LeaveStampCraftResponse from(String nickname) {
        return LeaveStampCraftResponse.builder().type(MessageType.LEAVE).nickname(nickname).build();
    }

    public static LeaveStampCraftResponse from(Member member, StampCraft stampCraft) {
        return LeaveStampCraftResponse.builder()
                .type(MessageType.LEAVE)
                .nickname(member.getNickname())
                .host(stampCraft.getHost().getNickname())
                .build();
    }
}
