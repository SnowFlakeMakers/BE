package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.CreateStampRequest;
import com.snowflakes.rednose.dto.stampcraft.CreateStampResponse;
import com.snowflakes.rednose.dto.stampcraft.EnterStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.LeaveStampCraftResponse;
import com.snowflakes.rednose.dto.stampcraft.PaintStampRequest;
import com.snowflakes.rednose.dto.stampcraft.PaintStampResponse;
import com.snowflakes.rednose.dto.stampcraft.ShowCreateStampProgressResponse;
import com.snowflakes.rednose.dto.stampcraft.StartStampNamingResponse;
import com.snowflakes.rednose.service.PreSignedUrlService;
import com.snowflakes.rednose.service.StampCraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StampCraftController {

    private final StampCraftService stampCraftService;
    private final PreSignedUrlService preSignedUrlService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/api/v1/stamp-craft")
    public CreateStampCraftResponse create(@RequestBody CreateStampCraftRequest request, @MemberId Long memberId) {
        return stampCraftService.create(request, memberId);
    }

    @EventListener
    public void connectWebSocket(SessionConnectEvent event) {
        stampCraftService.connect(event);
    }

    @EventListener
    public void disconnectWebSocket(SessionDisconnectEvent event) {
        stampCraftService.disconnect(event);
    }

    @MessageMapping("/stamp-craft/{stamp-craft-id}/enter")
    @SendTo("/sub/stamp-craft/{stamp-craft-id}")
    public EnterStampCraftResponse enter(@DestinationVariable("stamp-craft-id") Long stampCraftId,
                                         SimpMessageHeaderAccessor accessor) {
        return stampCraftService.enter(stampCraftId, accessor);
    }

    @MessageMapping("/stamp-craft/{stamp-craft-id}/paint")
    @SendTo("/sub/stamp-craft/{stamp-craft-id}")
    public PaintStampResponse paint(@DestinationVariable("stamp-craft-id") Long stampCraftId,
                                    @RequestBody PaintStampRequest request) {
        return stampCraftService.paint(stampCraftId, request);
    }

    @MessageMapping("/stamp-craft/{stamp-craft-id}/leave")
    @SendTo("/sub/stamp-craft/{stamp-craft-id}")
    public LeaveStampCraftResponse leave(@DestinationVariable("stamp-craft-id") Long stampCraftId,
                                         SimpMessageHeaderAccessor accessor) {
        return stampCraftService.leave(stampCraftId, accessor);
    }

    @GetMapping("/api/v1/stamps/pre-signed-url")
    public CreatePreSignedUrlResponse getPreSignedUrl() {
        return preSignedUrlService.getStampPreSignedUrlForPut();
    }

    @PostMapping("/api/v1/stamps/{stampCraftId}")
    public CreateStampResponse done(@RequestBody CreateStampRequest request, @MemberId Long memberId,
                                    @PathVariable Long stampCraftId) {
        CreateStampResponse response = stampCraftService.done(request, memberId, stampCraftId);
        messagingTemplate.convertAndSend("/sub/stamp-craft/" + stampCraftId, response);
        return response;
    }

    @GetMapping("/api/v1/stamp-craft/{stampCraftId}")
    public ShowCreateStampProgressResponse showProgress(@PathVariable Long stampCraftId) {
        return stampCraftService.getProgress(stampCraftId);
    }

    @PostMapping("/api/v1/stamp-craft/{stampCraftId}/naming")
    public StartStampNamingResponse naming(@PathVariable Long stampCraftId,
                                           @MemberId Long memberId) {
        StartStampNamingResponse response = stampCraftService.startNaming(stampCraftId, memberId);
        messagingTemplate.convertAndSend("/sub/stamp-craft/" + stampCraftId, response);
        return response;
    }

}
