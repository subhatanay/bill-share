package com.subadev.billshare.groupbillshare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class UserGetResponseDTO {
    private String userId;
    private String emailId;
    private String name;
    private UserStatusDTO status;
    private List<String> roles;
    private String token;
    private String picture_url;
    private boolean isGroupAdmin;
}
