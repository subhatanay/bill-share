package com.subhadev.billshare.userservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;

public class JwtAuthenticationFilter  extends AuthenticationFilter  {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager, new JwtAuthenticationConverter());

        this.setSuccessHandler(((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }));
    }
}
