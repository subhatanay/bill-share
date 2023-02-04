package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class GroupPUTRequestDTO implements Validation {
    private String groupName;
    private String description;
    @Override
    public void validate() throws ValidationException {
        if (groupName!=null) {
            if (!StringUtils.hasText(groupName)) {
                throw new ValidationException("Group name should not be empty or null");
            }
            if (groupName.length() > 255) {
                throw new ValidationException("Group name should be within 255 characters.");
            }
        }
        if(description !=null) {
            if (!StringUtils.hasText(description)) {
                throw new ValidationException("Group description should not be empty");
            }

            if (description.length() > 255) {
                throw new ValidationException("Group description should be within 255 characters.");
            }
        }
    }
}
