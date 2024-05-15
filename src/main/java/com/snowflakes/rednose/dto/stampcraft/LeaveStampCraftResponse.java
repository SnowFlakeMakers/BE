package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.StampCraft;
import lombok.Builder;

public class LeaveStampCraftResponse {

    private String nickname;
    private String host;

    @Builder
    public LeaveStampCraftResponse(String nickname, String host) {
        this.nickname = nickname;
        this.host = host;
    }

    public static LeaveStampCraftResponse from(String nickname) {
        return LeaveStampCraftResponse.builder().nickname(nickname).build();
    }

    public static LeaveStampCraftResponse from(Member member, StampCraft stampCraft) {
        return LeaveStampCraftResponse.builder()
                .nickname(member.getNickname())
                .host(stampCraft.getHost().getNickname())
                .build();
    }
}
