package com.snowflakes.rednose.dto.seal;

import com.snowflakes.rednose.entity.Seal;
import lombok.Builder;

public class SealResponse {

    private String image;
    private String name;
    private int numberOfLikes;

    @Builder
    public SealResponse(String image, String name, int numberOfLikes) {
        this.image = image;
        this.name = name;
        this.numberOfLikes = numberOfLikes;
    }

    public static SealResponse from(Seal seal) {
        return SealResponse.builder()
                .image(seal.getImageUrl())
                .name(seal.getName())
                .numberOfLikes(seal.getNumberOfLikes())
                .build();
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }


}
