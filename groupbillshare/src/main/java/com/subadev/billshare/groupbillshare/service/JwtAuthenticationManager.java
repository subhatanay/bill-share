package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.JwtAuthentication;
import com.subadev.billshare.groupbillshare.dto.UserGetResponseDTO;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationManager implements AuthenticationManager {
    private UserService userService;
    private JwtService jwtService;

    public JwtAuthenticationManager(UserService userService,JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthentication) {
            JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
            String userId = jwtService.findUsernameFromJWT((String) jwtAuthentication.getCredentials());
            UserGetResponseDTO userGetResponseDTO;
            try {
                userGetResponseDTO =  this.userService.getUserInfo(userId);
            } catch (NotFoundException notFoundException) {
                throw notFoundException;
            }
            jwtAuthentication.setUser(userGetResponseDTO);
            return jwtAuthentication;
        }
        return null;
    }
}
