package com.snowflakes.rednose.dto.stamp;

public class CreatePresignedUrlResponse {

    private String url;

    public String getUrl() {
        return url;
    }

    public CreatePresignedUrlResponse(String url) {
        this.url = url;
    }
}
