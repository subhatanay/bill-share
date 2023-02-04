package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.*;

public interface GroupService {
    GroupGETResponseDTO createGroup(GroupPOSTRequestDTO groupPOSTRequestDTO);
    void addUsersToGroup(String groupId, GroupUsersPOSTRequestDTO groupUsersPOSTRequestDTO);
    void removeUserFromGroup(String groupId, String userId);
    GroupGETResponseDTO updateGroupInfo(String groupId, GroupPUTRequestDTO groupPUTRequestDTO);
    void updateGroupUserAsAdmin(String groupId, String userId, boolean isAdmin);
    UserGetResponseDTO getGroupUserByGroupIdAndUserId(String groupId, String userId);
    void removeGroup(String groupId);
    boolean isUserBelongToGroup(String groupId, String userId);
    PagedResults<GroupGETResponseDTO> getGroupsUnderUser(String userId, Integer limit, Integer offset) ;
    PagedResults<UserGetResponseDTO> getUsersUnderGroup(String groupId, Integer limit , Integer offset);
}
