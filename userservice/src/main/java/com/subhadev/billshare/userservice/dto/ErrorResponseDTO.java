package com.subhadev.billshare.userservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private Integer errorCode;
    private String errorMessage;

}

