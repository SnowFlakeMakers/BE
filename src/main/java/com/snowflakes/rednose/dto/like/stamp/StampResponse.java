package com.snowflakes.rednose.dto.like.stamp;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;

public class StampResponse {

    private String image;
    private String name;
    private int numberOfLikes;

    @Builder
    public StampResponse(String image, String name, int numberOfLikes) {
        this.image = image;
        this.name = name;
        this.numberOfLikes = numberOfLikes;
    }

    public static StampResponse of(Stamp stamp) {
        return StampResponse.builder()
                .image(stamp.getImageUrl())
                .name(stamp.getName())
                .numberOfLikes(stamp.getNumberOfLikes())
                .build();
    }
}
