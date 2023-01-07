package com.subhadev.billshare.userservice.service;

public interface OtpService {

    String generateAndStoreTOPT(String userId);

    boolean verifyTOPT(String userId, String totp);

}
