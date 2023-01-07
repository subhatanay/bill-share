package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.exception.ValidationException;
import lombok.*;
import org.springframework.util.StringUtils;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginOTPRequestDTO extends UserLoginRequestDTO implements Validation {
    private String emailId;
    private String totp;

    @Override
    public void validate() throws ValidationException {
        super.validate();

        if (!StringUtils.hasText(totp)) {
            throw new ValidationException("Invalid OTP provided");
        }
    }
}
