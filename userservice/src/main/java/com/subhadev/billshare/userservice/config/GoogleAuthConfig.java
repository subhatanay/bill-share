package com.subhadev.billshare.userservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.authentication.google")
@Getter
@Setter
public class GoogleAuthConfig {
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String scope;
    private String auth_uri;
    private String token_uri;
    private String userinfo_uri;
}
