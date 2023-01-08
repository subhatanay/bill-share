package com.subhadev.billshare.userservice.controller;

import com.subhadev.billshare.userservice.dto.ErrorResponseDTO;
import com.subhadev.billshare.userservice.dto.UserGetResponseDTO;
import com.subhadev.billshare.userservice.dto.UserPatchRequestDTO;
import com.subhadev.billshare.userservice.exception.ForbiddenException;
import com.subhadev.billshare.userservice.exception.UserNotFoundException;
import com.subhadev.billshare.userservice.exception.ValidationException;
import com.subhadev.billshare.userservice.helpers.CurrentAuthenticationHolder;
import com.subhadev.billshare.userservice.service.UserDaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users/profile")
public class UserResourceController {
    private UserDaoService userDaoService;

    public UserResourceController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @RequestMapping(path="/info")
    public ResponseEntity getUserInfo() {
        UserGetResponseDTO userGetResponseDTO = CurrentAuthenticationHolder.getCurrentAuthenticationContext();
        return ResponseEntity.status(HttpStatus.OK).body(userGetResponseDTO);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity updateUserInfo(@PathVariable("userId") String userId, @RequestBody UserPatchRequestDTO userPatchRequestDTO) {
        userPatchRequestDTO.validate();
        String userIdFromToken = CurrentAuthenticationHolder.getCurrentAuthenticationContext().getUserId();
        if (!userIdFromToken.equals(userId)) {
            throw new ForbiddenException("Request forbidden");
        }
        UserGetResponseDTO userGetResponseDTO = userDaoService.updateUser(userId,userPatchRequestDTO);

        return ResponseEntity.ok(userGetResponseDTO);
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

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbidden(ForbiddenException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(ForbiddenException.ERROR_CODE))
                .body(ErrorResponseDTO.builder()
                        .errorCode(ForbiddenException.ERROR_CODE)
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
