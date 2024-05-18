package com.snowflakes.rednose.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.snowflakes.rednose.dto.response.ShowStampsResponse;
import com.snowflakes.rednose.dto.stamp.ShowMyStampsResponse;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    private final StampRepository stampRepository;

    public ShowStampsResponse show(Pageable pageable) {
        Page<Stamp> stamps = stampRepository.findAll(pageable);
        return ShowStampsResponse.from(stamps);
    }

    public ShowMyStampsResponse showMyStamps(Pageable pageable, Long memberId) {
        Slice<Stamp> stamps = stampRepository.findMyStampsByMemberId(memberId, pageable);
        return ShowMyStampsResponse.from(stamps);
    }
}
