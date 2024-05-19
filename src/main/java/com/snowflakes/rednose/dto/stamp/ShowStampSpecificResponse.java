package com.snowflakes.rednose.dto.stamp;


import com.snowflakes.rednose.entity.Member;
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
    private boolean liked;
}
