package com.subadev.billshare.groupbillshare.helpers;

import com.subadev.billshare.groupbillshare.dto.JwtAuthentication;
import com.subadev.billshare.groupbillshare.dto.UserGetResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentSecurityContextHolder {

    public static JwtAuthentication getAuthentication() {
        return  (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserGetResponseDTO getUserDetails() {
        return (UserGetResponseDTO) getAuthentication().getDetails();
    }

}
