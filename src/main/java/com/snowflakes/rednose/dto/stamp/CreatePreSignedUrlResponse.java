package com.snowflakes.rednose.dto.stamp;

public class CreatePreSignedUrlResponse {

    private String url;

    public String getUrl() {
        return url;
    }

    public CreatePreSignedUrlResponse(String url) {
        this.url = url;
    }
}
