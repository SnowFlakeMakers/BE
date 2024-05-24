package com.snowflakes.rednose.dto.seal;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowSealsResponse {

    private int totalPage;
    private int pageNumber;
    private List<SealResponse> contents;

    public ShowSealsResponse(int totalPage, int pageNumber, List<SealResponse> contents) {
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.contents = contents;
    }
}
