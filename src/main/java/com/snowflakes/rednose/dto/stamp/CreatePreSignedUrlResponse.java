package com.snowflakes.rednose.dto.stamp;

import lombok.Getter;

@Getter
public class CreatePreSignedUrlResponse {

    private String url;

    public CreatePreSignedUrlResponse(String url) {
        this.url = url;
    }
}
