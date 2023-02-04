package com.subadev.billshare.groupbillshare.dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtAuthentication implements Authentication {
    private String jwtToken;
    private UserGetResponseDTO userGetResponseDTO;

    public JwtAuthentication(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setUser(UserGetResponseDTO userGetResponseDTO) {
        this.userGetResponseDTO = userGetResponseDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userGetResponseDTO!=null) {
            Collection<GrantedAuthority> grantedAuthorityCollection = new ArrayList<>();
            grantedAuthorityCollection = userGetResponseDTO.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return grantedAuthorityCollection;
        }
        return null;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return userGetResponseDTO;
    }

    @Override
    public Object getPrincipal() {
        return userGetResponseDTO;
    }

    @Override
    public boolean isAuthenticated() {
        return userGetResponseDTO!=null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return userGetResponseDTO.getUserId();
    }
}
