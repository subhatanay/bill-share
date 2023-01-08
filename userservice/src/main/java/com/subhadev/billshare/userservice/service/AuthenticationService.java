package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.config.GoogleAuthConfig;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import com.subhadev.billshare.userservice.exception.AuthenticationException;

public interface AuthenticationService {

    String generateAuthorizeURL();

    String generateAccessTokenUsingAuthorizationCodeFlow(String code) throws AuthenticationException;

    UserGetResponseDTO getUserInfo(String token) throws AuthenticationException;

}
