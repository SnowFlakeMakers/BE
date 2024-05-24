package com.snowflakes.rednose.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PreSignedUrlService {

    private final static String STAMP_DIRECTORY_NAME = "stamp";
    private final static String SEAL_DIRECTORY_NAME = "seal";
    private final static String EXTENSION = ".png";
    private final static int EXPIRATION = 10;

    private final AmazonS3 amazonS3;

    @Value("${cloud.s3.bucket}")
    private String bucket;

    public CreatePreSignedUrlResponse getStampPreSignedUrlForPut() {
        String preSignedUrl = getPreSignedUrl(createPath(STAMP_DIRECTORY_NAME), HttpMethod.PUT);
        return new CreatePreSignedUrlResponse(preSignedUrl);
    }

    public CreatePreSignedUrlResponse getSealPreSignedUrlForPut() {
        String preSignedUrl = getPreSignedUrl(createPath(SEAL_DIRECTORY_NAME), HttpMethod.PUT);
        return new CreatePreSignedUrlResponse(preSignedUrl);
    }

    public String getPreSignedUrlForShow(String path) {
        return getPreSignedUrl(path, HttpMethod.GET);
    }

    private String getPreSignedUrl(String path, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(path, httpMethod);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String path, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, path)
                        .withMethod(httpMethod)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION);
        return new Date(calendar.getTimeInMillis());
    }

    private String createPath(String directoryName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(directoryName)
                .append("/")
                .append(UUID.randomUUID())
                .append(EXTENSION);
        return stringBuilder.toString();
    }
}
