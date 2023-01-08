package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.exception.ValidationException;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDTO extends UserLoginRequestDTO implements Validation {

    private String fullName;
    @Override
    public void validate() throws ValidationException {
        if (this.getFullName() == null || this.getFullName().isEmpty()) {
            throw new ValidationException("Full name should not be empty");
        }

        super.validate();
    }

    public static UserRegisterRequestDTO from(UserGetResponseDTO userGetResponseDTO) {
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
        userRegisterRequestDTO.setFullName(userGetResponseDTO.getName());
        userRegisterRequestDTO.setEmail(userGetResponseDTO.getEmailId());

        return userRegisterRequestDTO;
    }

}
