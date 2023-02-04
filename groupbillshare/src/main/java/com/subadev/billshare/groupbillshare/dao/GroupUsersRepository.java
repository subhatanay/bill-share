package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.GroupEntity;
import com.subadev.billshare.groupbillshare.entity.GroupUsersEntity;
import com.subadev.billshare.groupbillshare.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GroupUsersRepository extends JpaRepository<GroupUsersEntity, String> {

    @Query(value="SELECT gr from group_users gr WHERE gr.userInfo.userId=?1",
            countQuery = "SELECT count(gr.groupInfo) from group_users gr WHERE gr.userInfo.userId=?1")
    Page<GroupUsersEntity> getGroupsUnderUser(String userId, Pageable page);

    @Query(value="SELECT gu from  group_users gu WHERE gu.groupInfo.groupId=?1",
            countQuery = "SELECT count(gu) from  group_users gu WHERE gu.groupInfo.groupId=?1")
    Page<GroupUsersEntity> getUsersUnderGroup(String groupId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM group_users WHERE groupInfo.groupId=?1 AND userInfo.userId=?2")
    void deleteGroupUserByGroupIdAndUserId(String groupId, String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM group_users WHERE groupInfo.groupId=?1")
    void deleteGroupUserByGroupId(String groupId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE group_users SET isAdmin=?3 WHERE groupInfo.groupId=?1 AND userInfo.userId=?2")
    void updateGroupUserAdmin(String groupId, String userId, boolean isAdmin);
}
