package com.subadev.billshare.groupbillshare.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="group_users")
@IdClass(GroupUsersEntityId.class)
public class GroupUsersEntity extends AuditEntity {

    @Id
    @ManyToOne
    @JoinColumn(name="group_id", referencedColumnName = "groupId")
    private GroupEntity groupInfo;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "userId")
    private UserEntity userInfo;

    private Boolean isAdmin;

}
