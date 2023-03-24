package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.exception.ForbiddenException;
import com.subadev.billshare.groupbillshare.exception.InternalServerException;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service
public class GroupOnboardFacade {

    private static Logger logger = LoggerFactory.getLogger(GroupOnboardFacade.class);
    private GroupService groupService;
    private UserService userService;

    private GroupJoinService groupJoinService;

    public GroupOnboardFacade(GroupService groupService, UserService userService, GroupJoinService groupJoinService) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupJoinService = groupJoinService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public GroupGETResponseDTO createGroup(GroupPOSTRequestDTO groupPOSTRequestDTO, String currentUserId) {
        logger.info("Request for creating group for userId : " + currentUserId);
        GroupGETResponseDTO groupGETResponseDTO = groupService.createGroup(groupPOSTRequestDTO);

        GroupUsersPOSTRequestDTO groupUsersPOSTRequestDTO = GroupUsersPOSTRequestDTO.builder().build();
        UserRegisterRequestDTO userRegisterRequestDTO  = new UserRegisterRequestDTO();
        userRegisterRequestDTO.setUserId(currentUserId);
        userRegisterRequestDTO.setAdmin(true);
        groupUsersPOSTRequestDTO.setUsers(new ArrayList<>());
        groupUsersPOSTRequestDTO.getUsers().add(userRegisterRequestDTO);

        addUserInGroup(groupGETResponseDTO.getGroupId(),currentUserId,groupUsersPOSTRequestDTO, null, true);
        logger.info("Group created successfully");
        return groupGETResponseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void addUserInGroup(String groupId, String currentUserId, GroupUsersPOSTRequestDTO groupUsersPOSTRequestDTO, String token, boolean isFromCreateGroupOrJoinGroup) {
        if (!isFromCreateGroupOrJoinGroup) {
            UserGetResponseDTO currentUserInfo = groupService.getGroupUserByGroupIdAndUserId(groupId, currentUserId);
            if (!currentUserInfo.isGroupAdmin()) {
                throw new ForbiddenException("Current User not allowed to remove group. User needs to be group admin.");
            }
        }
        groupUsersPOSTRequestDTO.getUsers().stream().forEach(user -> {
             if (user.getUserId() == null) {
                 UserGetResponseDTO userGetResponseDTO;
                 try {
                     userGetResponseDTO = userService.getUserByEmail(user.getEmail());
                 } catch (NotFoundException notFoundException) {
                     logger.info("User not found under group " + groupId + ". Creating the user.");
                     userGetResponseDTO = userService.createUser(user,token);
                     logger.info("User created successfully under group " + groupId);
                 }
                 user.setUserId(userGetResponseDTO.getUserId());
             }
        });
        logger.info(groupUsersPOSTRequestDTO.getUsers().size() + " user going to add  under groupId " + groupId);
        groupService.addUsersToGroup(groupId,groupUsersPOSTRequestDTO);
        logger.info("Users successfully added to group " + groupId);
    }

    public void removeUserFromGroup(String groupId,String currentUserId, String userId) {
        if (!groupService.isUserBelongToGroup(groupId, currentUserId)) {
            throw new ForbiddenException("Current user not belong to the group " + groupId);
        }
        groupService.removeUserFromGroup(groupId, userId);
    }


    public void removeGroup(String groupId, String currentUserId) {
        UserGetResponseDTO currentUserInfo = groupService.getGroupUserByGroupIdAndUserId(groupId, currentUserId);
        if (!currentUserInfo.isGroupAdmin()) {
            throw new ForbiddenException("Current User not allowed to remove group. User needs to be group admin.");
        }
        groupService.removeGroup(groupId);
    }

    public PagedResults<GroupGETResponseDTO> getGroupsByUserId(String userId, Integer limit, Integer offset) {
        return groupService.getGroupsUnderUser(userId, limit, offset);
    }

    public PagedResults<UserGetResponseDTO> getUsersByGroupId(String groupId, String currentUserId , Integer limit, Integer offset) {
        if (!groupService.isUserBelongToGroup(groupId, currentUserId)) {
            throw new ForbiddenException("Current user not belong to the group " + groupId);
        }
        return groupService.getUsersUnderGroup(groupId, limit, offset);
    }

    public GroupGETResponseDTO updateGroupInfo(String groupId,String currentUserId, GroupPUTRequestDTO groupPUTRequestDTO) {
        if (!groupService.isUserBelongToGroup(groupId, currentUserId)) {
            throw new ForbiddenException("Current user not belong to the group " + groupId);
        }
        return groupService.updateGroupInfo(groupId, groupPUTRequestDTO);
    }

    public void updateGroupUserAdminStatus(String groupId, String currentUserId, String targetUserId, boolean isAdmin) {
        UserGetResponseDTO currentUserInfo = groupService.getGroupUserByGroupIdAndUserId(groupId, currentUserId);
        if (!currentUserInfo.isGroupAdmin()) {
            throw new ForbiddenException("Authentication user is not a group admin. Group user needs to be admin to make other user admin.");
        }
        groupService.updateGroupUserAsAdmin(groupId, targetUserId, isAdmin);
    }

    public String  generateJoinCode(String groupId,String currentUserId, boolean force) {
        UserGetResponseDTO currentUserInfo = groupService.getGroupUserByGroupIdAndUserId(groupId, currentUserId);
        if (!currentUserInfo.isGroupAdmin()) {
            throw new ForbiddenException("Authentication user is not a group admin. Group user needs to be admin to generate join code");
        }
        String joinCode = groupJoinService.generateJoinCodeForGroup(groupId, force);
        if (joinCode == null) {
            throw new InternalServerException("Not able to generate join code");
        }
        return joinCode;
    }

    public void removeJoinCode(String groupId, String currentUserId) {
        UserGetResponseDTO currentUserInfo = groupService.getGroupUserByGroupIdAndUserId(groupId, currentUserId);
        if (!currentUserInfo.isGroupAdmin()) {
            throw new ForbiddenException("Authentication user is not a group admin. Group user needs to be admin to generate join code");
        }
        groupJoinService.removeJoinCode(groupId);
    }

    public void joinCurrentUserToGroup(String currentUserId, String joinCode) {
        String groupId = groupJoinService.getGroupIdByJoinCode(joinCode);
        System.out.println(groupId);
        if (groupId == null) {
            throw new NotFoundException("Join code is invalid.");
        }
        GroupUsersPOSTRequestDTO groupUsersPOSTRequestDTO = GroupUsersPOSTRequestDTO.builder().build();
        UserRegisterRequestDTO userRegisterRequestDTO  = new UserRegisterRequestDTO();
        userRegisterRequestDTO.setUserId(currentUserId);
        userRegisterRequestDTO.setAdmin(false);
        groupUsersPOSTRequestDTO.setUsers(new ArrayList<>());
        groupUsersPOSTRequestDTO.getUsers().add(userRegisterRequestDTO);

        addUserInGroup(groupId, currentUserId,groupUsersPOSTRequestDTO,null , true );
    }


}
