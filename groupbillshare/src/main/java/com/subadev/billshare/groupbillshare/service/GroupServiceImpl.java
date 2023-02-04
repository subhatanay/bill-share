package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dao.GroupRepository;
import com.subadev.billshare.groupbillshare.dao.GroupUsersRepository;
import com.subadev.billshare.groupbillshare.dao.UserRepository;
import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.entity.GroupEntity;
import com.subadev.billshare.groupbillshare.entity.GroupUsersEntity;
import com.subadev.billshare.groupbillshare.entity.UserEntity;
import com.subadev.billshare.groupbillshare.exception.AlreadyExistsException;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    GroupUsersRepository groupUsersRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupUsersRepository groupUsersRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupUsersRepository = groupUsersRepository;
    }
    @Override
    public GroupGETResponseDTO createGroup(GroupPOSTRequestDTO groupPOSTRequestDTO) {
        GroupEntity groupEntity=GroupEntity.builder()
                .groupName(groupPOSTRequestDTO.getGroupName())
                .description(groupPOSTRequestDTO.getDescription())

                .build();
        groupEntity.setCreatedTime(new Date());
        groupEntity.setLastUpdatedTime(new Date());

        groupEntity = groupRepository.save(groupEntity);
        return GroupGETResponseDTO.builder()
                .groupId(groupEntity.getGroupId())
                .groupName(groupEntity.getGroupName())
                .description(groupPOSTRequestDTO.getDescription())
                .build();
    }
    @Override
    public void addUsersToGroup(String groupId, GroupUsersPOSTRequestDTO users) {
        final GroupEntity groupEntity = getGroupByGroupId(groupId);
        List<GroupUsersEntity> groupUsers = users.getUsers().stream().map(user -> {
            Optional<UserEntity> userEntity = userRepository.findById(user.getUserId());
            if (!userEntity.isPresent()) {
                throw new NotFoundException("User not found");
            }
            if (isUserBelongToGroup(groupId, user.getUserId())) {
                throw new AlreadyExistsException(MessageFormat.format("Group with groupId {0} and userId {1} already exists.", groupId, user.getUserId()));
            }
            GroupUsersEntity groupUsersEntity = GroupUsersEntity.builder().isAdmin(user.isAdmin()).groupInfo(groupEntity).userInfo(userEntity.get()).build();
            groupUsersEntity.setCreatedTime(new Date());
            groupUsersEntity.setLastUpdatedTime(new Date());
            return groupUsersEntity;
        }).collect(Collectors.toList());
        if ( groupEntity.getUsers() == null ) {
            groupEntity.setUsers(new ArrayList<>());
        }
        groupUsersRepository.saveAll(groupUsers);
        groupEntity.getUsers().addAll(groupUsers);
        groupRepository.save(groupEntity);
    }

    @Override
    public void removeUserFromGroup(String groupId, String userId) {
        if (!isUserBelongToGroup(groupId, userId)) {
            throw new NotFoundException("User not found under group " + groupId);
        }
        groupUsersRepository.deleteGroupUserByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public GroupGETResponseDTO updateGroupInfo(String groupId, GroupPUTRequestDTO groupPUTRequestDTO) {
        GroupEntity groupEntity = getGroupByGroupId(groupId);
        if (groupPUTRequestDTO.getGroupName() != null) {
            groupEntity.setGroupName(groupPUTRequestDTO.getGroupName());
        }
        if (groupPUTRequestDTO.getDescription() != null) {
            groupEntity.setDescription(groupPUTRequestDTO.getDescription());
        }
        groupRepository.save(groupEntity);
        return GroupGETResponseDTO.builder()
                .groupId(groupEntity.getGroupId())
                .groupName(groupEntity.getGroupName())
                .description(groupEntity.getDescription())
                .build();
    }
    @Override
    public void updateGroupUserAsAdmin(String groupId, String userId, boolean isAdmin) {
        if (!isUserBelongToGroup(groupId, userId)) {
            throw new NotFoundException("User not found under group " + groupId);
        }
        groupUsersRepository.updateGroupUserAdmin(groupId, userId, isAdmin);
    }

    @Override
    @Transactional
    public void removeGroup(String groupId) {
        GroupEntity groupEntity = getGroupByGroupId(groupId);
        groupUsersRepository.deleteGroupUserByGroupId(groupEntity.getGroupId());
        groupRepository.deleteGroupByGroupId(groupEntity.getGroupId());
    }

    @Override
    public boolean isUserBelongToGroup(String groupId, String userId) {
        return groupRepository.isUserPresentBelongToGroup(groupId, userId);
    }

    public UserGetResponseDTO getGroupUserByGroupIdAndUserId(String groupId, String userId) {
        if (!isUserBelongToGroup(groupId, userId)) {
            throw new NotFoundException("User not found under group " + groupId);
        }
        Optional<GroupUsersEntity> groupUsersEntityOpt = groupRepository.getUserUnderGroup(groupId, userId);
        GroupUsersEntity groupUsersEntity = groupUsersEntityOpt.get();
        return UserGetResponseDTO.builder()
                .name(groupUsersEntity.getUserInfo().getName())
                .userId(groupUsersEntity.getUserInfo().getUserId())
                .emailId(groupUsersEntity.getUserInfo().getEmailId())
                .picture_url(groupUsersEntity.getUserInfo().getProfilePic())
                .status(UserStatusDTO.from(groupUsersEntity.getUserInfo().getStatus()))
                .isGroupAdmin(groupUsersEntity.getIsAdmin())
                .build();
    }

    @Override
    public PagedResults<GroupGETResponseDTO> getGroupsUnderUser(String userId, Integer limit, Integer offset) {
        Page<GroupUsersEntity> groupEntities = groupUsersRepository.getGroupsUnderUser(userId, Pageable.ofSize(limit).withPage(offset));

        if (groupEntities == null || groupEntities.getTotalElements() == 0) {
            throw new NotFoundException(MessageFormat.format("No Groups found under user with userId : {0}",userId));
        }
        System.out.println("--> " + groupEntities.getContent().size());

        PagedResults<GroupGETResponseDTO> groupGETResponseDTOPagedResults = PagedResults.<GroupGETResponseDTO>builder()
                .pageSize(limit)
                .totalCount((int) groupEntities.getTotalElements())
                .pageCount(groupEntities.getTotalPages())
                .results(groupEntities.getContent().stream().map(group -> {
                        return GroupGETResponseDTO
                                .builder()
                                .groupId(group.getGroupInfo().getGroupId())
                                .groupName(group.getGroupInfo().getGroupName())
                                .description(group.getGroupInfo().getDescription())
                                .groupAdmin(group.getIsAdmin())
                                .build();
                        })
                        .collect(Collectors.toList()))

                .build();
        return groupGETResponseDTOPagedResults;
    }

    @Override
    public PagedResults<UserGetResponseDTO> getUsersUnderGroup(String groupId, Integer limit, Integer offset) {
        GroupEntity groupEntity = getGroupByGroupId(groupId);
        Page<GroupUsersEntity>  groupUsersEntities  = groupUsersRepository.getUsersUnderGroup(groupEntity.getGroupId(), Pageable.ofSize(limit).withPage(offset));

        if (groupUsersEntities == null || groupUsersEntities.getTotalElements() ==0 ){
            throw new NotFoundException(MessageFormat.format("No User found under the group with group Id {0}",groupId));
        }
        PagedResults<UserGetResponseDTO> userGetResponseDTOPagedResults = PagedResults.<UserGetResponseDTO>builder()
                .pageSize(limit)
                .pageCount(groupUsersEntities.getTotalPages())
                .totalCount((int)groupUsersEntities.getTotalElements())
                .results(groupUsersEntities.getContent().stream()
                        .map(userGroup -> UserGetResponseDTO.builder()
                                    .name(userGroup.getUserInfo().getName())
                                    .userId(userGroup.getUserInfo().getUserId())
                                    .emailId(userGroup.getUserInfo().getEmailId())
                                    .picture_url(userGroup.getUserInfo().getProfilePic())
                                    .status(UserStatusDTO.from(userGroup.getUserInfo().getStatus()))
                                    .isGroupAdmin(userGroup.getIsAdmin())
                                    .build())
                        .collect(Collectors.toList()))
                .build();
        return userGetResponseDTOPagedResults;
    }


    private GroupEntity getGroupByGroupId(String groupId) {
        Optional<GroupEntity> groupEntityOpt = groupRepository.findById(groupId);
        if (!groupEntityOpt.isPresent()) {
            throw new NotFoundException("Group not found");
        }
        return groupEntityOpt.get();
    }
}
