package com.subadev.billshare.groupbillshare.helpers;

import java.security.SecureRandom;

public class JoinCodeGenerator {
    private static Integer OTP_LENGTH = 6;
    private static String UPPER_MASK = "abcdefghijklmnopqrstuvwxyz";
    private static String LOWER_MASK = UPPER_MASK.toLowerCase();
    public static String generateJoinCode() {
        SecureRandom secureRandom = new SecureRandom();
        String joinCode = "";
        for(int i = 0;i < OTP_LENGTH; i++) {
            joinCode += UPPER_MASK.charAt(secureRandom.nextInt(26));
            joinCode += LOWER_MASK.charAt(secureRandom.nextInt(26));
        }
        return joinCode;

    }

}
