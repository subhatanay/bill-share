package com.subhadev.billshare.userservice.facade;

import com.subhadev.billshare.userservice.dto.*;
import com.subhadev.billshare.userservice.exception.InvalidEmailOTPException;
import com.subhadev.billshare.userservice.service.JwtService;
import com.subhadev.billshare.userservice.service.NotificationService;
import com.subhadev.billshare.userservice.service.OtpService;
import com.subhadev.billshare.userservice.service.UserDaoService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserFacade {
    private UserDaoService userDaoService;
    private OtpService otpService;
    private NotificationService notificationService;
    private JwtService jwtService;

    public UserFacade(UserDaoService userDaoService,
                      OtpService otpService,
                      NotificationService notificationService,
                      JwtService jwtService) {
        this.userDaoService = userDaoService;
        this.otpService = otpService;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    public UserGetResponseDTO registerUserByEmail(UserRegisterRequestDTO userRegisterRequestDTO) {
        UserGetResponseDTO userGetResponseDTO = this.userDaoService.registerUser(userRegisterRequestDTO,UserStatusDTO.CREATED);
        String userTOPT = this.otpService.generateAndStoreTOPT(userGetResponseDTO.getUserId());
        this.notificationService.sendNotificationEvent(userGetResponseDTO.getEmailId(),NotificationType.USER_ON_BOARD_SUCCESS_BY_EMAIL, Map.of("fullName", userGetResponseDTO.getName(),"totp", userTOPT));

        return userGetResponseDTO;
    }

    public UserGetResponseDTO loginUserByEmail(UserLoginRequestDTO userLoginRequestDTO) {
        UserGetResponseDTO userGetResponseDTO = this.userDaoService.findUserByEmail(userLoginRequestDTO.getEmail());
        String userTOPT = this.otpService.generateAndStoreTOPT(userGetResponseDTO.getUserId());
        this.notificationService.sendNotificationEvent(userGetResponseDTO.getEmailId(),NotificationType.USER_ON_BOARD_SUCCESS_BY_EMAIL, Map.of("fullName", userGetResponseDTO.getName(),"totp", userTOPT));

        return userGetResponseDTO;
    }

    public UserGetResponseDTO authenticateUserByEmailOTP(UserLoginOTPRequestDTO userLoginOTPRequestDTO) {
        UserGetResponseDTO userGetResponseDTO = this.userDaoService.findUserByEmail(userLoginOTPRequestDTO.getEmail());
        boolean isOtpMatched = this.otpService.verifyTOPT(userGetResponseDTO.getUserId(), userLoginOTPRequestDTO.getTotp());
        if (!isOtpMatched) {
            throw new InvalidEmailOTPException("Provided OPT does not matched");
        }
        if (userGetResponseDTO.getStatus().equals(UserStatusDTO.CREATED)) {
            this.userDaoService.updateUserStatus(userLoginOTPRequestDTO.getEmail(), UserStatusDTO.VERIFIED);
            userGetResponseDTO.setStatus(UserStatusDTO.VERIFIED);
        }
        String token = jwtService.createJwt(userGetResponseDTO.getUserId(), userGetResponseDTO.getRoles());
        userGetResponseDTO.setToken(token);

        return userGetResponseDTO;
    }
}
