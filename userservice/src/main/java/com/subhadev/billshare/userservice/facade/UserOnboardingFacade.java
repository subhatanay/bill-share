package com.subhadev.billshare.userservice.facade;

import com.subhadev.billshare.userservice.dto.*;
import com.subhadev.billshare.userservice.exception.InvalidEmailOTPException;
import com.subhadev.billshare.userservice.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserOnboardingFacade {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthenticationService.class);
    private UserDaoService userDaoService;
    private OtpService otpService;
    private NotificationService notificationService;
    private JwtService jwtService;

    private AuthenticationService authenticationService;

    public UserOnboardingFacade(UserDaoService userDaoService,
                                OtpService otpService,
                                NotificationService notificationService,
                                JwtService jwtService,
                                AuthenticationService authenticationService) {
        this.userDaoService = userDaoService;
        this.otpService = otpService;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    public UserGetResponseDTO registerUserByEmail(UserRegisterRequestDTO userRegisterRequestDTO) {
        logger.info("User Register request using Email");
        UserGetResponseDTO userGetResponseDTO = this.userDaoService.registerUser(userRegisterRequestDTO,UserStatusDTO.CREATED);
        String userTOPT = this.otpService.generateAndStoreTOPT(userGetResponseDTO.getUserId());
        this.notificationService.sendNotificationEvent(userGetResponseDTO.getEmailId(),NotificationType.USER_ON_BOARD_SUCCESS_BY_EMAIL, Map.of("fullName", userGetResponseDTO.getName(),"totp", userTOPT));

        return UserGetResponseDTO.builder().userId(userGetResponseDTO.getUserId()).build();
    }

    public UserGetResponseDTO loginUserByEmail(UserLoginRequestDTO userLoginRequestDTO) {
        logger.info("User Login request using Email");
        UserGetResponseDTO userGetResponseDTO = this.userDaoService.findUserByEmail(userLoginRequestDTO.getEmail());
        String userTOPT = this.otpService.generateAndStoreTOPT(userGetResponseDTO.getUserId());
        this.notificationService.sendNotificationEvent(userGetResponseDTO.getEmailId(),NotificationType.USER_OTP_EMAIL, Map.of("totp", userTOPT));

        return UserGetResponseDTO.builder().userId(userGetResponseDTO.getUserId()).build();
    }

    public UserGetResponseDTO authenticateUserByEmailOTP(UserLoginOTPRequestDTO userLoginOTPRequestDTO) {
        logger.info("User Verify OTP Request using Email");
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

    public UserGetResponseDTO authenticateUserByGoogle(String authorizationCode) {
        logger.info("Authenticate Request using Google provider");
        String googleAccessToken = authenticationService.generateAccessTokenUsingAuthorizationCodeFlow(authorizationCode);
        UserGetResponseDTO userInfo = authenticationService.getUserInfo(googleAccessToken);

        boolean isUserExists = userDaoService.isUserExistsByEmail(userInfo.getEmailId());
        if (!isUserExists) {
            userInfo = this.userDaoService.registerUser(UserRegisterRequestDTO.from(userInfo),UserStatusDTO.VERIFIED);
            this.notificationService.sendNotificationEvent(userInfo.getEmailId(),NotificationType.USER_ON_BOARD_SUCCESS_BY_GOOGLE, Map.of("fullName", userInfo.getName()));
        } else {
            userInfo = userDaoService.findUserByEmail(userInfo.getEmailId());
        }
        String token = jwtService.createJwt(userInfo.getUserId(), userInfo.getRoles());
        userInfo.setToken(token);
        return userInfo;
    }
}
