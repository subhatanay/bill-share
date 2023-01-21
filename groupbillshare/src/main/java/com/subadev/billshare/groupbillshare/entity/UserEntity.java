package com.subadev.billshare.groupbillshare.entity;



import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity extends AuditEntity {
    @Id
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String  userId;
    @Column(name = "email",length = 255, unique = true, nullable = false)
    private String emailId;
    @Column(name= "name" , nullable = true)
    private String name;
    @Column(name= "profile_pic" , nullable = true)
    private String profilePic;
    @Column(name= "user_status" , nullable = false)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> userRoles;

    @OneToMany(mappedBy = "groupInfo")
    private List<GroupUsersEntity> groups;
}
