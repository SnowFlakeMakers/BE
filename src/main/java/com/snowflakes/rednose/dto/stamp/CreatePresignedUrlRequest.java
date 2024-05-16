package com.snowflakes.rednose.dto.stamp;

public class CreatePresignedUrlRequest {

    private String fileName;
    private String directoryName;

    public CreatePresignedUrlRequest() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
