package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.stamp.ShowStampSpecificResponse;
import com.snowflakes.rednose.dto.stamp.ShowStampsResponse;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.StampErrorCode;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.StampRecordRepository;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class StampService {

    private final StampRepository stampRepository;
    private final StampLikeRepository stampLikeRepository;
    private final StampRecordRepository stampRecordRepository;

    public ShowStampsResponse show(Pageable pageable) {
        Page<Stamp> stamps = stampRepository.findAll(pageable);
        return ShowStampsResponse.from(stamps);
    }

    public ShowStampSpecificResponse showSpecific(Long stampId, Long memberId) {
        Stamp stamp = stampRepository.findById(stampId)
                .orElseThrow(() -> new NotFoundException(StampErrorCode.NOT_FOUND));

        boolean liked = stampLikeRepository.existsByMemberIdAndStampId(memberId, stampId);

        List<String> collaborators = stampRecordRepository.findAllByStamp(stamp).stream()
                .map((record) -> record.getMember().getNickname()).collect(
                        Collectors.toList());

        return ShowStampSpecificResponse.of(stamp, liked, collaborators);
    }
}
