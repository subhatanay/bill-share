package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class UserPatchRequestDTO implements Validation {
    private String pictureUri;
    private String fullName;

    @Override
    public void validate() throws ValidationException {
        if (pictureUri !=null && !StringUtils.hasText(pictureUri))  {
            throw new ValidationException("Picture url should not be empty");
        }
        if (fullName !=null && !StringUtils.hasText(fullName)) {
            throw new ValidationException("Full name should not be empty");
        }
    }



}
