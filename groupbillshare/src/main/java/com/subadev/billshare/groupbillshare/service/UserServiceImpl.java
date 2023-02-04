package com.subadev.billshare.groupbillshare.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subadev.billshare.groupbillshare.dao.UserRepository;
import com.subadev.billshare.groupbillshare.dto.UserGetResponseDTO;
import com.subadev.billshare.groupbillshare.dto.UserRegisterRequestDTO;
import com.subadev.billshare.groupbillshare.dto.UserStatusDTO;
import com.subadev.billshare.groupbillshare.entity.RoleEntity;
import com.subadev.billshare.groupbillshare.entity.UserEntity;
import com.subadev.billshare.groupbillshare.dto.ErrorResponseDTO;
import com.subadev.billshare.groupbillshare.exception.InternalServerException;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RestTemplate restTemplate  = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserGetResponseDTO createUser(UserRegisterRequestDTO registerRequestDTO, String token) {
        try {
            String createUserURI = "http://localhost:5050/userservice/users";
            String registerRequestDTOJson = objectMapper.writeValueAsString(registerRequestDTO);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> requestEntity = new HttpEntity<>(registerRequestDTOJson,headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(createUserURI,requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                UserGetResponseDTO userGetResponseDTO = objectMapper.readValue(responseEntity.getBody(),UserGetResponseDTO.class);
                return userGetResponseDTO;
            }
            ErrorResponseDTO errorResponseDTO = objectMapper.readValue(responseEntity.getBody(), ErrorResponseDTO.class);
            throw new InternalServerException(errorResponseDTO.getErrorMessage());
        } catch (JsonProcessingException jsonProcessingException) {
            throw new InternalServerException(jsonProcessingException.getMessage());
        }
    }

    @Override
    public UserGetResponseDTO getUserInfo(String userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return  getUser(userEntity);
    }

    @Override
    public UserGetResponseDTO getUserByEmail(String emailId) {
        Optional<UserEntity> userEntity = userRepository.findByEmailId(emailId);
        return  getUser(userEntity);
    }

    private UserGetResponseDTO getUser(Optional<UserEntity> userEntity) {
        if (!userEntity.isPresent()) {
            throw new NotFoundException("User not found");
        }
        return UserGetResponseDTO.builder()
                .userId(userEntity.get().getUserId())
                .emailId(userEntity.get().getEmailId())
                .roles(userEntity.get().getUserRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList()))
                .name(userEntity.get().getName())
                .status(UserStatusDTO.from(userEntity.get().getStatus()))
                .build();
    }


}
