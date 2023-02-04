package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.GroupJoinCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface GroupJoinCodeRepository extends CrudRepository<GroupJoinCodeEntity, String> {


    Optional<GroupJoinCodeEntity> findByGroupId(String s);
}
