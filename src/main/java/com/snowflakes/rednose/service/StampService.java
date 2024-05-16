package com.snowflakes.rednose.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.snowflakes.rednose.dto.response.ShowStampsResponse;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class StampService {

    @Value("${cloud.s3.bucket}")
    private String bucket;

    private final StampRepository stampRepository;
    private final AmazonS3 amazonS3;


    public ShowStampsResponse show(Pageable pageable) {
        Page<Stamp> stamps = stampRepository.findAll(pageable);
        return ShowStampsResponse.from(stamps);
    }

    public CreatePreSignedUrlResponse getPreSignedUrl(CreatePreSignedUrlRequest request) {
        String path = createPath(request);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(path);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return new CreatePreSignedUrlResponse(url.toString());
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String path) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, path)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        return new Date(calendar.getTimeInMillis());
    }


    private String createPath(CreatePreSignedUrlRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!request.getDirectoryName().isEmpty()) {
            stringBuilder.append(request.getDirectoryName())
                    .append("/");
        }
        return stringBuilder.append(UUID.randomUUID()).append(request.getFileName()).toString();
    }
}
