package com.subadev.billshare.groupbillshare.helpers;

import java.util.regex.Pattern;

public class EmailValidator {
    private final static String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}";

    public static boolean validateEmail(String email) {
        return Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE).matcher(email).find();
    }

}
