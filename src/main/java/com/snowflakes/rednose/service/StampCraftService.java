package com.snowflakes.rednose.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.dto.stampcraft.CreateStampRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.CreateStampResponse;
import com.snowflakes.rednose.dto.stampcraft.EnterStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.LeaveStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.PaintStampRequest;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampCraft;
import com.snowflakes.rednose.entity.StampRecord;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.exception.errorcode.StampCraftErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampRecordRepository;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StampCraftService {

    private final MemberRepository memberRepository;
    private final StampRecordRepository stampRecordRepository;
    private final StampRepository stampRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.s3.bucket}")
    private String bucket;

    private Long ID = 0L;
    private Map<Long, StampCraft> stampCrafts = new ConcurrentHashMap<>();
    private Map<String, Long> connections = new ConcurrentHashMap<>();

    @Transactional
    public CreateStampCraftResponse create(CreateStampCraftRequest request, Long memberId) {
        Member member = findMemberById(memberId);
        StampCraft stampCraft = StampCraft.builder().host(member).canvasType(request.getCanvasType()).build();
        stampCrafts.put(ID, stampCraft);
        return CreateStampCraftResponse.from(ID++);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    public void paint(Long stampCraftId, PaintStampRequest request) {
        validExistStampCraft(stampCraftId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        stampCraft.paint(request.getX(), request.getY(), request.getColor());
        stampCrafts.put(stampCraftId, stampCraft);
    }

    private void validExistStampCraft(Long stampCraftId) {
        if (!stampCrafts.containsKey(stampCraftId)) {
            throw new NotFoundException(StampCraftErrorCode.NOT_FOUND);
        }
    }

    public EnterStampCraftResponse enter(Long stampCraftId, SimpMessageHeaderAccessor accessor) {
        Long memberId = connections.get(accessor.getSessionId());
        Member member = findMemberById(memberId);
        validExistStampCraft(stampCraftId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        stampCraft.enter(member);
        return EnterStampCraftResponse.from(member);
    }

    public LeaveStampCraftResponse leave(Long stampCraftId, SimpMessageHeaderAccessor accessor) {
        Long memberId = connections.get(accessor.getSessionId());
        Member member = findMemberById(memberId);
        validExistStampCraft(stampCraftId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        stampCraft.quit(member);
        if (!stampCraft.hasMembers()) {
            stampCrafts.remove(stampCraft);
        }
        if (stampCraft.hasHost(member)) {
            stampCraft.chooseNewHost();
        }
        return LeaveStampCraftResponse.from(member, stampCraft);
    }

    @Transactional
    public CreateStampResponse done(CreateStampRequest request, Long memberId, Long stampCraftId) {
        Member host = findMemberById(memberId);
        validExistStampCraft(stampCraftId);
        StampCraft stampCraft = stampCrafts.get(stampCraftId);
        validCorrectHost(host, stampCraft);
        Stamp stamp = stampRepository.save(request.toStamp());
        for (Member member : stampCraft.getMembers()) {
            StampRecord stampRecord = StampRecord.builder().stamp(stamp).member(member).build();
            stampRecordRepository.save(stampRecord);
        }
        stampCrafts.remove(stampCraftId);
        return CreateStampResponse.from(stamp);
    }

    private void validCorrectHost(Member host, StampCraft stampCraft) {
        if(!stampCraft.hasHost(host)) {
            throw new BadRequestException(StampCraftErrorCode.NOT_HOST);
        }
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

    public void connect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Long memberId = Long.parseLong(headerAccessor.getFirstNativeHeader("MemberId"));
        connections.put(sessionId, memberId);
    }

    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        connections.remove(sessionId);
    }
}
