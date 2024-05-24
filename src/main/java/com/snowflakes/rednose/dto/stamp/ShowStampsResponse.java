package com.snowflakes.rednose.dto.stamp;

import com.snowflakes.rednose.entity.Stamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class ShowStampsResponse {

    private List<StampAtListResponse> stampList;
    private int pageNumber;
    private int totalPages;

    public static ShowStampsResponse from(Page<Stamp> stamps) {
        return ShowStampsResponse.builder()
                .pageNumber(stamps.getPageable().getPageNumber())
                .totalPages(stamps.getTotalPages())
                .stampList(stamps.getContent().stream().map(StampAtListResponse::from).collect(Collectors.toList()))
                .build();
    }
}
