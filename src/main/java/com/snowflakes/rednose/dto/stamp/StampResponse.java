package com.snowflakes.rednose.dto.stamp;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import lombok.Getter;

@Getter
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

    public static StampResponse from(Stamp stamp) {
        return StampResponse.builder()
                .image(stamp.getImageUrl())
                .name(stamp.getName())
                .numberOfLikes(stamp.getNumberOfLikes())
                .build();
    }

    public static StampResponse of(Stamp stamp, String imageUrl) {
        return StampResponse.builder()
                .image(imageUrl)
                .name(stamp.getName())
                .numberOfLikes(stamp.getNumberOfLikes())
                .build();
    }
}
