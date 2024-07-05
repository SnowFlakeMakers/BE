package com.snowflakes.rednose.config;

import com.snowflakes.rednose.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof CustomException) {
            StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
            accessor.setLeaveMutable(true);
            accessor.setMessage(ex.getMessage());
            return MessageBuilder.createMessage(cause.getMessage().getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);

    }
}
