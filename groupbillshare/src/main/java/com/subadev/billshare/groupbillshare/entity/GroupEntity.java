package com.subadev.billshare.groupbillshare.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class GroupEntity extends AuditEntity {
    @Id
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    private String groupId;
    private String groupName;
    private String description;

    @OneToMany(mappedBy = "userInfo")
    private List<GroupUsersEntity> users;

    @OneToMany(mappedBy = "group")
    private List<ExpenseEntity> expenses;
}
