package com.subadev.billshare.groupbillshare.controller;

import com.subadev.billshare.groupbillshare.dto.ErrorResponseDTO;
import com.subadev.billshare.groupbillshare.dto.GroupJoinResponseDTO;
import com.subadev.billshare.groupbillshare.helpers.CurrentSecurityContextHolder;
import com.subadev.billshare.groupbillshare.service.GroupOnboardFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupJoinServiceController {

    GroupOnboardFacade groupOnboardFacade;

    public  GroupJoinServiceController(GroupOnboardFacade groupOnboardFacade) {
        this.groupOnboardFacade = groupOnboardFacade;
    }

    @PostMapping("/{groupId}/join-code")
    public ResponseEntity generateJoinCode(@PathVariable("groupId") String groupId, @RequestParam(name="force", defaultValue = "false") boolean force) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        String joinCode = groupOnboardFacade.generateJoinCode(groupId,currentUserId, force);
        GroupJoinResponseDTO groupJoinResponseDTO = GroupJoinResponseDTO.builder().joinCode(joinCode).build();
        return ResponseEntity.ok(groupJoinResponseDTO);
    }

    @DeleteMapping("/{groupId}/join-code")
    public ResponseEntity removeJoinCode(@PathVariable("groupId") String groupId) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.removeJoinCode(groupId,currentUserId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/join/{joinCode}")
    public ResponseEntity joinGroupByJoinCode(@PathVariable("joinCode") String joinCode) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.joinCurrentUserToGroup(currentUserId, joinCode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.valueOf(500))
                .body(ErrorResponseDTO.builder()
                        .errorCode(500)
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }
}
