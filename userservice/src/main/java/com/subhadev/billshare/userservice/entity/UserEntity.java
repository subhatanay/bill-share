package com.subhadev.billshare.userservice.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String userId;
    @Column(name = "email",length = 255, unique = true, nullable = false)
    private String emailId;
    @Column(name= "name" , nullable = true)
    private String name;
    @Column(name= "profile_pic" , nullable = true)
    private String profilePic;
    @Column(name= "user_status" , nullable = false)
    private UserStatus status;
    @OneToMany
    @JoinTable(name="user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> userRoles;

}
