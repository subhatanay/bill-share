package com.subadev.billshare.groupbillshare.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="roles")
public class RoleEntity extends AuditEntity {
    public static String USER_ADMIN = "USER_ADMIN";
    public static String GROUP_ADMIN = "GROUP_ADMIN";
    public static String SYS_ADMIN = "SYS_ADMIN";

    @Id
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String roleId;
    @Column(name="role_name", nullable = false, length = 255, unique = true)
    private String roleName;
}
