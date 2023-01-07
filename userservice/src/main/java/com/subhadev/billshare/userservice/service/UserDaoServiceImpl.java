package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dao.RoleRepository;
import com.subhadev.billshare.userservice.dao.UserRepository;
import com.subhadev.billshare.userservice.dto.*;
import com.subhadev.billshare.userservice.entity.RoleEntity;
import com.subhadev.billshare.userservice.entity.UserEntity;
import com.subhadev.billshare.userservice.entity.UserStatus;
import com.subhadev.billshare.userservice.exception.UserAlreadyExistsException;
import com.subhadev.billshare.userservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDaoServiceImpl implements UserDaoService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserDaoServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserGetResponseDTO registerUser(UserRegisterRequestDTO userPostRequestDTO, UserStatusDTO userStatusDTO) {
        if (userRepository.existsByEmailId(userPostRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException(MessageFormat.format("User with emailId {0} already exists", userPostRequestDTO.getEmail()));
        }

        RoleEntity roleEntity = this.roleRepository.findByRoleName(RoleEntity.USER_ADMIN);
        UserEntity userEntity = UserEntity.from(userPostRequestDTO, userStatusDTO);
        userEntity.setUserRoles(Set.of(roleEntity));
        userEntity = userRepository.save(userEntity);

        return UserGetResponseDTO
                .builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .emailId(userEntity.getEmailId())
                .status(userStatusDTO)
                .build();
    }

    @Override
    public UserGetResponseDTO findUserByEmail(String email) {
        UserEntity userEntity = getUserByEmail(email);
        return  UserGetResponseDTO.builder()
                .userId(userEntity.getUserId())
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .emailId(userEntity.getEmailId())
                .roles(userEntity.getUserRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList()))
                .status(UserStatusDTO.from(userEntity.getStatus()))
                .build();
    }

    @Override
    public void updateUserStatus(String email, UserStatusDTO userStatusDTO) {
        UserEntity userEntity = getUserByEmail(email);
        userEntity.setStatus(UserStatus.from(userStatusDTO));
        this.userRepository.save(userEntity);
    }

    private UserEntity getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email can't be null");
        }
        Optional<UserEntity> userEntity  = userRepository.findByEmailId(email);
        if (!userEntity.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        return userEntity.get();
    }
}
