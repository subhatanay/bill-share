package com.subhadev.billshare.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessTokenResponseDTO {
   private String access_token;
   private String expires_in;
   private String refresh_token;
   private String scope;
   private String token_type;
   private String id_token;

}