package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import com.subhadev.billshare.userservice.dto.UserRegisterRequestDTO;
import com.subhadev.billshare.userservice.dto.UserStatusDTO;

public interface UserDaoService {

    UserGetResponseDTO registerUser(UserRegisterRequestDTO userPostRequestDTO, UserStatusDTO userStatusDTO);

    UserGetResponseDTO findUserByEmail(String email);

    void updateUserStatus(String email, UserStatusDTO userStatusDTO);
}
