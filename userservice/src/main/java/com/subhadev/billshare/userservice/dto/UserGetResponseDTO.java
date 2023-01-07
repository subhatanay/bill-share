package com.subhadev.billshare.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGetResponseDTO {
    private String userId;
    private String emailId;
    private String name;
    private UserStatusDTO status;
    private List<String> roles;
    private String token;
}
