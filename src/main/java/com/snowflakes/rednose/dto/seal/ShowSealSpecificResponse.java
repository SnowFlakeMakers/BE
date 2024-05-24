package com.snowflakes.rednose.dto.seal;


import com.snowflakes.rednose.entity.Seal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowSealSpecificResponse {
    private String imageUrl;
    private String name;
    private int likes;
    private boolean liked;
    private LocalDateTime createdAt;
    private String creator;

    public static ShowSealSpecificResponse of(Seal seal, boolean liked) {
        return ShowSealSpecificResponse.builder().imageUrl(seal.getImageUrl()).name(seal.getName())
                .likes(seal.getNumberOfLikes())
                .createdAt(seal.getCreatedAt()).creator(seal.getMember().getNickname()).liked(liked).build();
    }
}
