package com.subhadev.billshare.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserRegisterResponseDTO {

    private String userId;
    private String emailId;
    private String name;
    private UserStatusDTO status;
    private String token;


}
