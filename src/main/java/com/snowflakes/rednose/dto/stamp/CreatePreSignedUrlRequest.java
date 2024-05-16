package com.snowflakes.rednose.dto.stamp;

public class CreatePreSignedUrlRequest {

    private String fileName;
    private String directoryName;

    public CreatePreSignedUrlRequest() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
