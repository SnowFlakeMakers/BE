package com.snowflakes.rednose.dto.response;

import com.snowflakes.rednose.entity.Stamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Builder
@Getter
public class ShowStampsResponse {

    private List<StampAtListResponse> stampList;

    public static ShowStampsResponse from(Slice<Stamp> stamps) {
        List<Stamp> content = stamps.getContent();
        return ShowStampsResponse.builder()
                .stampList(content.stream().map(StampAtListResponse::from).collect(Collectors.toList()))
                .build();
    }
}
