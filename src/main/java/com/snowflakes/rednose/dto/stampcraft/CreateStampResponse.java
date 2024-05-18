package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;

public class CreateStampResponse {

    private String image;
    private String name;
    private Long id;

    @Builder
    public CreateStampResponse(String image, String name, Long id) {
        this.image = image;
        this.name = name;
        this.id = id;
    }

    public static CreateStampResponse from(Stamp stamp) {
        return CreateStampResponse.builder()
                .image(stamp.getImageUrl())
                .name(stamp.getName())
                .id(stamp.getId())
                .build();
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
