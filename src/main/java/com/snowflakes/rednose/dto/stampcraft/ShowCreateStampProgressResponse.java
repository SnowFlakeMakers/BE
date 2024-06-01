package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.StampCraft;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShowCreateStampProgressResponse {

    private long remain;
    private String[][] stamp;
    private String host;
    private int numberOfMembers;

    @Builder
    public ShowCreateStampProgressResponse(long remain, String[][] stamp, String host, int numberOfMembers) {
        this.remain = remain;
        this.stamp = stamp;
        this.host = host;
        this.numberOfMembers = numberOfMembers;
    }

    public static ShowCreateStampProgressResponse of(StampCraft stampCraft, long remain) {
        return ShowCreateStampProgressResponse.builder()
                .stamp(stampCraft.getStamp())
                .numberOfMembers(stampCraft.getMembers().size())
                .remain(remain)
                .host(stampCraft.getHost().getNickname())
                .build();
    }
}
