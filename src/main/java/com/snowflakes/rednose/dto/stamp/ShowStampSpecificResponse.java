package com.snowflakes.rednose.dto.stamp;


import com.snowflakes.rednose.entity.Member;
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
    private List<Member> collaborators;
    private int numberOfLikes;
    private boolean liked;

    public static ShowStampSpecificResponse of(Stamp stamp, boolean liked, List<Member> collaborators) {
        return ShowStampSpecificResponse.builder().name(stamp.getName()).imageUrl(stamp.getImageUrl())
                .createdAt(stamp.getCreatedAt()).collaborators(collaborators).numberOfLikes(stamp.getNumberOfLikes()).liked(liked).build();
    }
}
