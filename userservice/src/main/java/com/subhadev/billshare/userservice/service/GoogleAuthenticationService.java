package com.subhadev.billshare.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subhadev.billshare.userservice.config.GoogleAuthConfig;
import com.subhadev.billshare.userservice.dto.AccessTokenResponseDTO;
import com.subhadev.billshare.userservice.dto.AuthorizeCodeRequestDTO;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import com.subhadev.billshare.userservice.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthenticationService  implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthenticationService.class);
    private GoogleAuthConfig googleAuthConfig;
    private RestTemplate restTemplate;
    ObjectMapper mapper = new ObjectMapper();

    public GoogleAuthenticationService(GoogleAuthConfig googleAuthConfig) {
        this.googleAuthConfig = googleAuthConfig;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String generateAuthorizeURL() {
        return MessageFormat.format(googleAuthConfig.getAuth_uri(), googleAuthConfig.getClient_id(),googleAuthConfig.getRedirect_uri(),googleAuthConfig.getScope());
    }

    @Override
    public String generateAccessTokenUsingAuthorizationCodeFlow(String code) throws AuthenticationException {
        try {
            AuthorizeCodeRequestDTO authorizeCodeRequestDTO = AuthorizeCodeRequestDTO.builder()
                    .code(code)
                    .client_id(googleAuthConfig.getClient_id())
                    .client_secret(googleAuthConfig.getClient_secret())
                    .grant_type("authorization_code")
                    .redirect_uri(googleAuthConfig.getRedirect_uri())
                    .build();

            String authorizePostBody = mapper.writeValueAsString(authorizeCodeRequestDTO);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(authorizePostBody,headers);
            ResponseEntity<AccessTokenResponseDTO> accessTokenResponseDTOResponse = restTemplate.postForEntity(googleAuthConfig.getToken_uri(),requestEntity, AccessTokenResponseDTO.class);
            if (accessTokenResponseDTOResponse.getStatusCode().equals(HttpStatus.OK)) {
                AccessTokenResponseDTO accessTokenResponseDTO =  accessTokenResponseDTOResponse.getBody();
                return accessTokenResponseDTO.getAccess_token();
            }
        } catch (JsonProcessingException jsonProcessingException) {
            logger.error("Error while authenticating with Google via authorization_code flow . Error reason : " + jsonProcessingException.getMessage());
        }
        throw new AuthenticationException("Invalid Authorization code provided");
    }

    @Override
    public UserGetResponseDTO getUserInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(googleAuthConfig.getUserinfo_uri(),HttpMethod.GET, requestEntity, Map.class);
        if (userInfoResponse.getStatusCode().equals(HttpStatus.OK)) {
            Map<String, Object> googleUserInfo = userInfoResponse.getBody();
            return UserGetResponseDTO.builder()
                    .emailId((String)googleUserInfo.get("email"))
                    .name((String)googleUserInfo.get("name"))
                    .picture_url((String)googleUserInfo.get("picture"))
                    .build();
        }
        throw new AuthenticationException("Invalid access token provided");
    }
}
