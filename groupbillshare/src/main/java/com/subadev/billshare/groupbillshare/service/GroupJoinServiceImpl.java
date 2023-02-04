package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dao.GroupJoinCodeRepository;
import com.subadev.billshare.groupbillshare.entity.GroupJoinCodeEntity;
import com.subadev.billshare.groupbillshare.helpers.JoinCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupJoinServiceImpl implements  GroupJoinService {

    GroupJoinCodeRepository groupJoinCodeRepository;

    public GroupJoinServiceImpl(GroupJoinCodeRepository groupJoinCodeRepository) {
        this.groupJoinCodeRepository = groupJoinCodeRepository;
    }

    @Override
    public String generateJoinCodeForGroup(String groupId, boolean force) {
        String joinCode = getJoinCodeByGroupId(groupId);
        if (joinCode == null || force) {
            joinCode = JoinCodeGenerator.generateJoinCode();
            GroupJoinCodeEntity groupJoinCodeEntity = GroupJoinCodeEntity.builder().id(joinCode).groupId(groupId).joinCode(joinCode).build();
            groupJoinCodeRepository.save(groupJoinCodeEntity);
        }
        return joinCode;
    }

    @Override
    public String getJoinCodeByGroupId(String groupId) {
        Optional<GroupJoinCodeEntity> groupJoinCodeEntityOptional = groupJoinCodeRepository.findByGroupId(groupId);
        if (!groupJoinCodeEntityOptional.isPresent()) {
            return null;
        }
        return groupJoinCodeEntityOptional.get().getJoinCode();
    }

    @Override
    public String getGroupIdByJoinCode(String joinCode) {
        Optional<GroupJoinCodeEntity> groupJoinCodeEntityOptional = groupJoinCodeRepository.findById(joinCode);
        if (!groupJoinCodeEntityOptional.isPresent()) {
            return null;
        }
        return groupJoinCodeEntityOptional.get().getGroupId();
    }

    @Override
    public void removeJoinCode(String groupId) {
        Optional<GroupJoinCodeEntity> groupJoinCodeEntityOptional = groupJoinCodeRepository.findById(groupId);
        if (!groupJoinCodeEntityOptional.isPresent()) {
            return ;
        }
        groupJoinCodeRepository.deleteById(groupId);
    }
}
