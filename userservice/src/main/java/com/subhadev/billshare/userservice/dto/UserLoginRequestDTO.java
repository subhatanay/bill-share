package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.exception.ValidationException;
import com.subhadev.billshare.userservice.helpers.EmailValidator;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDTO implements Validation {
    private String email;

    @Override
    public void validate() throws ValidationException {
        if (this.getEmail() == null || this.getEmail().isEmpty()) {
            throw new ValidationException("Email should not be empty");
        }

        if (!EmailValidator.validateEmail(email)) {
            throw new ValidationException("Email should be valid");
        }
    }
}
