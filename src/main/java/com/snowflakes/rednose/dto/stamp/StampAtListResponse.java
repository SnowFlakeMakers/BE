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

    public static StampAtListResponse from(Stamp stamp) {
        return StampAtListResponse.builder().name(stamp.getName())
                .image(stamp.getImageUrl())
                .likes(stamp.getNumberOfLikes())
                .id(stamp.getId())
                .build();
    }
}
