package com.snowflakes.rednose.dto.stamp;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StampAtListResponse {
    private String name;
    private String image;
    private int likes;
    private Long id;

    public static StampAtListResponse of(Stamp stamp, String preSignedUrl) {
        return StampAtListResponse.builder().name(stamp.getName())
                .image(preSignedUrl)
                .likes(stamp.getNumberOfLikes())
                .id(stamp.getId())
                .build();
    }
}
