package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.exception.ValidationException;
import com.subadev.billshare.groupbillshare.helpers.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDTO  implements Validation {

    private String fullName;
    private String email;

    private String userId;

    private boolean isAdmin = false;

    @Override
    public void validate() throws ValidationException {
        if (this.getFullName() == null || this.getFullName().isEmpty()) {
            throw new ValidationException("Full name should not be empty");
        }

        if (this.getEmail() == null || this.getEmail().isEmpty()) {
            throw new ValidationException("Email should not be empty");
        }

        if (!EmailValidator.validateEmail(email)) {
            throw new ValidationException("Email should be valid");
        }
    }

    public static UserRegisterRequestDTO from(UserGetResponseDTO userGetResponseDTO) {
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
        userRegisterRequestDTO.setFullName(userGetResponseDTO.getName());
        userRegisterRequestDTO.setEmail(userGetResponseDTO.getEmailId());

        return userRegisterRequestDTO;
    }

}
