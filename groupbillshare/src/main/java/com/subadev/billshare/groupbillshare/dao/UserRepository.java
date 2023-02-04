package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String > {

    Optional<UserEntity> findByEmailId(String emailId);

}
