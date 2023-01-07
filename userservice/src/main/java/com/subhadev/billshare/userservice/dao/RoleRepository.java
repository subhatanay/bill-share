package com.subhadev.billshare.userservice.dao;

import com.subhadev.billshare.userservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    RoleEntity findByRoleName(String roleName);

}
