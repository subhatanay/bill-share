package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dto.JwtAuthentication;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationManager implements AuthenticationManager {
    private JwtService jwtService;
    private UserDaoService userDaoService;

    public JwtAuthenticationManager(JwtService jwtService, UserDaoService userDaoService) {
        this.jwtService = jwtService;
        this.userDaoService = userDaoService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;

        String userId = jwtService.findUsernameFromJWT((String) jwtAuthentication.getCredentials());

        UserGetResponseDTO userGetResponseDTO =  userDaoService.findUserByUserId(userId);
        jwtAuthentication.setUser(userGetResponseDTO);
        return jwtAuthentication;
    }
}
