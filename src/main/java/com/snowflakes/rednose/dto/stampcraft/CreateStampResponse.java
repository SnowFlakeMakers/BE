package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import lombok.Getter;

import static com.snowflakes.rednose.dto.stampcraft.MessageType.DONE;

@Getter
public class CreateStampResponse {

    private String image;
    private String name;
    private Long id;
    private MessageType messageType;

    @Builder
    public CreateStampResponse(String image, String name, Long id, MessageType messageType) {
        this.image = image;
        this.name = name;
        this.id = id;
        this.messageType = messageType;
    }

    public static CreateStampResponse of(Stamp stamp, String image) {
        return CreateStampResponse.builder()
                .image(image)
                .name(stamp.getName())
                .id(stamp.getId())
                .messageType(DONE)
                .build();
    }

}
