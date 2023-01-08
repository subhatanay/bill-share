package com.subhadev.billshare.userservice.helpers;

import com.subhadev.billshare.userservice.dto.JwtAuthentication;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentAuthenticationHolder {

    public static UserGetResponseDTO getCurrentAuthenticationContext() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        UserGetResponseDTO authTokenInfo = (UserGetResponseDTO) jwtAuthentication.getDetails();

        return authTokenInfo;
    }

    public static boolean isAuthenticatedRequest() {
        return SecurityContextHolder.getContext().getAuthentication() == null || (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthentication) ;
    }
}
