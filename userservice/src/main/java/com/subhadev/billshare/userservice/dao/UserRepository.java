package com.subhadev.billshare.userservice.dao;

import com.subhadev.billshare.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmailId(String emailId);
    Optional<UserEntity> findByEmailId(String emailId);

}
