package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

public class JwtAuthenticationEntryPoint implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String jwtString  = request.getHeader("Authorization");
        if (jwtString ==null || !jwtString.startsWith("Bearer")) {
            return null;
        }
        jwtString = jwtString.substring(7);
        return new JwtAuthentication(jwtString);
    }
}
