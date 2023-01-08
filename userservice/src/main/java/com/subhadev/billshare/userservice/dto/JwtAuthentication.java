package com.subhadev.billshare.userservice.dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthentication implements Authentication {

    private UserGetResponseDTO userGetResponseDTO;
    private String jwtString;

    public JwtAuthentication( String jwtString) {
        this.jwtString = jwtString;
    }

    public void setUser(UserGetResponseDTO userGetResponseDTO) {
        this.userGetResponseDTO = userGetResponseDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userGetResponseDTO!=null) {
            List<SimpleGrantedAuthority> roleList = new ArrayList<>();
            for (String role : userGetResponseDTO.getRoles()) {
                roleList.add(new SimpleGrantedAuthority(role));
            }
            return roleList;
        }
        return null;
    }

    @Override
    public Object getCredentials() {
        return jwtString;
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
