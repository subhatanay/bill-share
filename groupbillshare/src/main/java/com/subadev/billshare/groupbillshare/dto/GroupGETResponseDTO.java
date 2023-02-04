package com.subadev.billshare.groupbillshare.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupGETResponseDTO {

    private String groupId;
    private String groupName;
    private String description;
    private boolean groupAdmin;

}
