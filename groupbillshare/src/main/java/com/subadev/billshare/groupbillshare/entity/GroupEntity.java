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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    private String groupId;
    private String groupName;
    private String description;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.REMOVE)
    private List<GroupUsersEntity> users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<ExpenseEntity> expenses;
}
