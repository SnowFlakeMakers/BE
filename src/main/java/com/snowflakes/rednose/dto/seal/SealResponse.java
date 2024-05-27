package com.snowflakes.rednose.dto.seal;

import com.snowflakes.rednose.entity.Seal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SealResponse {

    private String image;
    private String name;
    private int numberOfLikes;
    private Long id;

    @Builder
    public SealResponse(String image, String name, int numberOfLikes, Long id) {
        this.image = image;
        this.name = name;
        this.numberOfLikes = numberOfLikes;
        this.id = id;
    }

    public static SealResponse of(Seal seal, String preSignedUrl) {
        return SealResponse.builder()
                .image(preSignedUrl)
                .name(seal.getName())
                .numberOfLikes(seal.getNumberOfLikes())
                .id(seal.getId())
                .build();
    }

}
