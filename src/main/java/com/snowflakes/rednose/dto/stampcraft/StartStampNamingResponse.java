package com.snowflakes.rednose.dto.stampcraft;

import lombok.Getter;

@Getter
public class StartStampNamingResponse {

    private MessageType type;

    public StartStampNamingResponse() {
        this.type = MessageType.NAMING;
    }
}
