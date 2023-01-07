package com.subhadev.billshare.userservice.dao;

import com.subhadev.billshare.userservice.entity.OtpEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends CrudRepository<OtpEntity, String> {
}
