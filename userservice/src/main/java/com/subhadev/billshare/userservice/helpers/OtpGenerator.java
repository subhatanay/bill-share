package com.subhadev.billshare.userservice.helpers;

import java.security.SecureRandom;

public class OtpGenerator {
    private static Integer OTP_LENGTH = 6;

    public static String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        String otpString = "";
        for(int i = 0;i < OTP_LENGTH; i++) {
            otpString += secureRandom.nextInt(10);
        }
        return otpString;
    }
}
