package com.subhadev.billshare.userservice.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="roles")
public class RoleEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String roleId;
    @Column(name="role_name", nullable = false, length = 255, unique = true)
    private String roleName;
}
