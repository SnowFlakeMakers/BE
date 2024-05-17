package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.response.ShowStampsResponse;
import com.snowflakes.rednose.dto.stamp.ShowMyStampsResponse;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
