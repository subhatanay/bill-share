package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.GroupEntity;
import com.subadev.billshare.groupbillshare.entity.GroupUsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, String> {

    @Query(nativeQuery = true,
            value ="SELECT count(gu) > 0 FROM  group_users gu WHERE gu.group_id=?1 and gu.user_id=?2" )
    boolean isUserPresentBelongToGroup(String groupId, String userId);



    @Query(value = "SELECT gu from  group_users gu WHERE gu.userInfo.userId=?2 AND gu.groupInfo.groupId=?1")
    Optional<GroupUsersEntity> getUserUnderGroup(String groupId, String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM groups WHERE groupId=?1")
    void deleteGroupByGroupId(String groupId);




}
