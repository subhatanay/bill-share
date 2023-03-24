package com.subadev.billshare.groupbillshare.controller;


import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.helpers.CurrentSecurityContextHolder;
import com.subadev.billshare.groupbillshare.service.ExpenseSettlementService;
import com.subadev.billshare.groupbillshare.service.GroupExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups/{groupId}")
public class GroupExpenseController {
    private GroupExpenseService groupExpenseService;
    private ExpenseSettlementService expenseSettlementService;

    public GroupExpenseController(GroupExpenseService groupExpenseService,
                                  ExpenseSettlementService expenseSettlementService) {
        this.groupExpenseService = groupExpenseService;
        this.expenseSettlementService = expenseSettlementService;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public ResponseEntity createExpense(@PathVariable("groupId") String groupId, @RequestBody GroupExpenseRequestDTO groupExpenseRequestDTO) {
        GroupExpenseResponseDTO groupExpenseResponseDTO = groupExpenseService.createExpense(groupId, groupExpenseRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(groupExpenseResponseDTO);
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public ResponseEntity getExpensesByGroupId(@PathVariable("groupId") String groupId, @RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(value = "offset", defaultValue = "0") int offset) {
        PagedResults<GroupExpenseGetResponseDTO> groupExpenseGetResponseDTO = groupExpenseService.fetchExpensesByGroupId(groupId,limit,offset);
        return ResponseEntity.status(HttpStatus.OK).body(groupExpenseGetResponseDTO);
    }

    @RequestMapping(value = "/expenses/{expenseId}", method = RequestMethod.GET)
    public ResponseEntity getExpensesByGroupId(@PathVariable("groupId") String groupId,@PathVariable("expenseId") String expenseId) {
        GroupExpenseGetResponseDTO groupExpenseGetResponseDTO = groupExpenseService.fetchExpenseByGroupIdAndExpenseId(groupId, expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(groupExpenseGetResponseDTO);
    }

    @RequestMapping(value = "/expenses/{expenseId}", method = RequestMethod.PUT)
    public ResponseEntity updateExpense(@PathVariable("groupId") String groupId, @PathVariable("expenseId") String expenseId, @RequestBody GroupExpensePUTRequestDTO groupExpensePUTRequestDTO) {
        GroupExpenseGetResponseDTO groupExpenseGetResponseDTO = groupExpenseService.updateExpense(groupId, expenseId, groupExpensePUTRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(groupExpenseGetResponseDTO);
    }

    @RequestMapping(value = "/expenses/{expenseId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteExpense(@PathVariable("groupId") String groupId, @PathVariable("expenseId") String expenseId) {
        groupExpenseService.deleteExpense(groupId, expenseId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/expenses/settlement/info", method = RequestMethod.GET)
    public ResponseEntity getSettlementInfoPerGroup(@PathVariable("groupId") String groupId) {
        String currentUserID = CurrentSecurityContextHolder.getUserDetails().getUserId();
        GroupExpenseSettlementResponseDTO groupExpenseSettlementResponseDTO = expenseSettlementService.getSettlementInfoByGroupIdAndCurrentUserId(groupId, currentUserID);
        return ResponseEntity.ok(groupExpenseSettlementResponseDTO);
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
