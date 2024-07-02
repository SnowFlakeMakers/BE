package com.snowflakes.rednose.config;

import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.service.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.SEND;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String accessToken = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        if (accessToken == null || accessToken.isEmpty()) {
            throw new UnAuthorizedException(AuthErrorCode.NULL_OR_BLANK_TOKEN);
        }
        jwtTokenProvider.verifySignature(accessToken);

        String sessionId = headerAccessor.getSessionId();
        Long memberId = jwtTokenProvider.getMemberId(accessToken);
        switch (headerAccessor.getCommand()) {
            case CONNECT:
                log.info("connect sessionId => {}, memberId => {}", sessionId, memberId);
                break;
            case SEND:
                log.info("send message sessionId => {}, memberId => {}", sessionId, memberId);
                break;
            case DISCONNECT:
                log.info("disconnect sessionId => {}, memberId => {}", sessionId, memberId);
                break;
            default:
                break;
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();

        if (headerAccessor.getCommand() == SEND) {
            if (sent) {
                log.info("send successfully sessionId => {}", sessionId);
            }
        }
    }
}
