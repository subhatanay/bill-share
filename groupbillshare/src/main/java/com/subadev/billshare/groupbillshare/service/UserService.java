package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.UserGetResponseDTO;
import com.subadev.billshare.groupbillshare.dto.UserRegisterRequestDTO;

public interface UserService {

    UserGetResponseDTO createUser(UserRegisterRequestDTO registerRequestDTO, String token);
    UserGetResponseDTO getUserInfo(String userId);

    UserGetResponseDTO getUserByEmail(String emailId);

}
