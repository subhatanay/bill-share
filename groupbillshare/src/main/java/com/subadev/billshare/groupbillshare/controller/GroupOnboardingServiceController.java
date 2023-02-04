package com.subadev.billshare.groupbillshare.controller;

import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.helpers.CurrentSecurityContextHolder;
import com.subadev.billshare.groupbillshare.service.GroupOnboardFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupOnboardingServiceController {
    GroupOnboardFacade groupOnboardFacade;
    public GroupOnboardingServiceController(GroupOnboardFacade groupOnboardFacade) {
        this.groupOnboardFacade = groupOnboardFacade;
    }

    @PostMapping
    public ResponseEntity createGroup(@RequestBody  GroupPOSTRequestDTO groupPOSTRequestDTO) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupPOSTRequestDTO.validate();
        GroupGETResponseDTO groupGETResponseDTO = groupOnboardFacade.createGroup(groupPOSTRequestDTO, currentUserId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupGETResponseDTO);
    }
    @PostMapping("/{groupId}/users")
    public ResponseEntity addUsersInGroup(@PathVariable("groupId") String groupId,@RequestBody GroupUsersPOSTRequestDTO groupUsersPOSTRequestDTO) {
        UserGetResponseDTO userGetResponseDTO = CurrentSecurityContextHolder.getUserDetails();
        groupUsersPOSTRequestDTO.validate();
        groupOnboardFacade.addUserInGroup(groupId,userGetResponseDTO.getUserId(), groupUsersPOSTRequestDTO, userGetResponseDTO.getToken(),false);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity getGroupsByCurrentUser(@RequestParam(value = "limit", defaultValue = "10") Integer limit,  @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        PagedResults<GroupGETResponseDTO> groupGETResponseDTOPagedResults = groupOnboardFacade.getGroupsByUserId(currentUserId, limit, offset);
        return ResponseEntity.ok(groupGETResponseDTOPagedResults);
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity getGroupUsersByGroupId(@PathVariable("groupId") String groupId,@RequestParam(value = "limit", defaultValue = "10") Integer limit,  @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        PagedResults<UserGetResponseDTO> userGetResponseDTOPagedResults = groupOnboardFacade.getUsersByGroupId(groupId,currentUserId, limit, offset);
        return ResponseEntity.ok(userGetResponseDTOPagedResults);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity updateGroupInfo(@PathVariable("groupId") String groupId, @RequestBody GroupPUTRequestDTO groupPUTRequestDTO) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        GroupGETResponseDTO groupGETResponseDTO = groupOnboardFacade.updateGroupInfo(groupId,currentUserId, groupPUTRequestDTO);
        return ResponseEntity.ok(groupGETResponseDTO);
    }

    @PutMapping("/{groupId}/users/{userId}/admin")
    public ResponseEntity markGroupUserAsAdminStatusON(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.updateGroupUserAdminStatus(groupId, currentUserId, userId, true);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{groupId}/users/{userId}/admin")
    public ResponseEntity markGroupUserAsAdminStatusOFF(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.updateGroupUserAdminStatus(groupId, currentUserId, userId, false);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity removeUserFromGroup(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.removeUserFromGroup(groupId, currentUserId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity removeGroup(@PathVariable("groupId") String groupId) {
        String currentUserId = CurrentSecurityContextHolder.getUserDetails().getUserId();
        groupOnboardFacade.removeGroup(groupId, currentUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
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
