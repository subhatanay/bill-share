package com.subhadev.billshare.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subhadev.billshare.userservice.dto.UserPatchRequestDTO;
import com.subhadev.billshare.userservice.dto.UserRegisterRequestDTO;
import com.subhadev.billshare.userservice.dto.UserStatusDTO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

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


    public static UserEntity from(UserRegisterRequestDTO userRegisterRequestDTO, UserStatusDTO userStatusDTO) {
        UserEntity userEntity =  UserEntity.builder()
                .emailId(userRegisterRequestDTO.getEmail())
                .name(userRegisterRequestDTO.getFullName())
                .status(UserStatus.from(userStatusDTO))

                .build();

        userEntity.setCreatedTime(new Date());
        userEntity.setLastUpdatedTime(new Date());
        return userEntity;
    }

    public  UserEntity from(UserPatchRequestDTO userPatchRequestDTO) {
        if (userPatchRequestDTO.getPictureUri() != null) {
            this.setProfilePic(userPatchRequestDTO.getPictureUri());
        }
        if (userPatchRequestDTO.getFullName() != null) {
            this.setName(userPatchRequestDTO.getFullName());
        }
        this.setLastUpdatedTime(new Date());
        return this;
    }



}
