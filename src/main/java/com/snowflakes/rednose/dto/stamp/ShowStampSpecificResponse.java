package com.snowflakes.rednose.dto.stamp;


import com.snowflakes.rednose.entity.Stamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowStampSpecificResponse {
    private String name;
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<String> collaborators;
    private int numberOfLikes;
    private boolean liked;
    private Long id;

    public static ShowStampSpecificResponse of(Stamp stamp, boolean liked, List<String> collaborators) {
        return ShowStampSpecificResponse.builder().name(stamp.getName()).imageUrl(stamp.getImageUrl())
                .createdAt(stamp.getCreatedAt()).collaborators(collaborators).numberOfLikes(stamp.getNumberOfLikes())
                .liked(liked)
                .id(stamp.getId())
                .build();
    }
}
