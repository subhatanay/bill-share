package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.exception.ValidationException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUsersPOSTRequestDTO implements Validation {
    private List<UserRegisterRequestDTO> users;
    @Override
    public void validate() throws ValidationException {
        if (users == null || users.size() == 0) {
            throw new ValidationException("Add at-least one user to the group.");
        }
        if (users.size() > 10) {
            throw new ValidationException("User list size can't be more than 10 for a single group add request.");
        }

        users.stream().forEach(user -> user.validate());
    }
}
