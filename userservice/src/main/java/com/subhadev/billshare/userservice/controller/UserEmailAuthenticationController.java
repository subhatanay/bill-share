package com.subhadev.billshare.userservice.controller;

import com.subhadev.billshare.userservice.dto.*;
import com.subhadev.billshare.userservice.exception.InvalidEmailOTPException;
import com.subhadev.billshare.userservice.exception.UserAlreadyExistsException;
import com.subhadev.billshare.userservice.exception.UserNotFoundException;
import com.subhadev.billshare.userservice.exception.ValidationException;
import com.subhadev.billshare.userservice.facade.UserFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserEmailAuthenticationController {

    private UserFacade userFacade;

    public UserEmailAuthenticationController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @RequestMapping(path = "/email/register", method = RequestMethod.POST)
    public ResponseEntity registerUserByEmail(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        userRegisterRequestDTO.validate();

        UserGetResponseDTO userGetResponseDTO = userFacade.registerUserByEmail(userRegisterRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userGetResponseDTO);
    }

    @RequestMapping(path="/email/login", method = RequestMethod.POST)
    public ResponseEntity loginUserByEmail(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        userLoginRequestDTO.validate();

        UserGetResponseDTO userGetResponseDTO = userFacade.loginUserByEmail(userLoginRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userGetResponseDTO);
    }

    @RequestMapping(path="/email/verify-otp", method = RequestMethod.POST)
    public ResponseEntity authenticateUserByOTP(@RequestBody UserLoginOTPRequestDTO userLoginOTPRequestDTO) {
        userLoginOTPRequestDTO.validate();

        UserGetResponseDTO userGetResponseDTO = userFacade.authenticateUserByEmailOTP(userLoginOTPRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userGetResponseDTO);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(UserAlreadyExistsException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(UserAlreadyExistsException.ERROR_CODE)
                        .errorMessage(exception.getMessage())
                        .build()
                );
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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> requestValidationException(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(ValidationException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(ValidationException.ERROR_CODE)
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(InvalidEmailOTPException.class)
    public ResponseEntity<ErrorResponseDTO> requestValidationException(InvalidEmailOTPException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(InvalidEmailOTPException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(InvalidEmailOTPException.ERROR_CODE)
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
