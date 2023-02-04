package com.subadev.billshare.groupbillshare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResults<T> {

    private Integer totalCount;
    private Integer pageSize;
    private Integer pageCount;

    List<T> results;
}

