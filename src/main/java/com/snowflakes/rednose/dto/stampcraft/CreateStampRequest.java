package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Stamp;

public class CreateStampRequest {

    private String image;
    private String name;

    protected CreateStampRequest() {
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Stamp toStamp() {
        return Stamp.builder()
                .name(name)
                .imageUrl(image)
                .build();
    }
}
