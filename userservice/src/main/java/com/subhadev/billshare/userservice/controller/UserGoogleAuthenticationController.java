package com.subhadev.billshare.userservice.controller;

import com.subhadev.billshare.userservice.dto.ErrorResponseDTO;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import com.subhadev.billshare.userservice.exception.AuthenticationException;
import com.subhadev.billshare.userservice.exception.UserNotFoundException;
import com.subhadev.billshare.userservice.facade.UserOnboardingFacade;
import com.subhadev.billshare.userservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/google")
public class UserGoogleAuthenticationController {
    private AuthenticationService authenticationService;
    private UserOnboardingFacade userOnboardingFacade;

    public   UserGoogleAuthenticationController(AuthenticationService authenticationService, UserOnboardingFacade userOnboardingFacade) {
        this.authenticationService = authenticationService;
        this.userOnboardingFacade = userOnboardingFacade;
    }

    @RequestMapping("/authorize")
    public ResponseEntity authorizeGoogleServer() {
        String googleAuthUrl = authenticationService.generateAuthorizeURL();
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", googleAuthUrl)
                .build();
    }

    @RequestMapping("/authcallback")
    public ResponseEntity authenticateGoogleAuthorizationCodeFlow(@RequestParam("code") String code) {
        UserGetResponseDTO userGetResponseDTO = userOnboardingFacade.authenticateUserByGoogle(code);
        return ResponseEntity.status(HttpStatus.OK).body(userGetResponseDTO);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(UserNotFoundException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(UserNotFoundException.ERROR_CODE)
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> requestValidationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(AuthenticationException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(AuthenticationException.ERROR_CODE)
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleCommonException(RuntimeException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }

}
