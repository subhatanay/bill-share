package com.subadev.billshare.groupbillshare.service.settlement;

import com.subadev.billshare.groupbillshare.entity.ExpenseEntity;
import com.subadev.billshare.groupbillshare.service.ExpenseSettlementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PerTransactionExpenseSettlement extends ExpenseSettlement {
    private static Logger logger = LoggerFactory.getLogger(PerTransactionExpenseSettlement.class);

    @Override
    public Map<String, Map<String, Double>> getSettlementDetails(List<ExpenseEntity> transactions) {
        Map<String,Map<String,Double>> settlementTransactions = new TreeMap<>();
        Map<String, Double> owesMap = new TreeMap<>();
        Map<String, Set<String>> dependentUserMap = new TreeMap<>();

        for (ExpenseEntity transaction : transactions) {


            logger.info("1. Calculate total expense and share value for expense id "  + transaction.getExpenseId());
            Double totalAmount = transaction.getExpenseDetails().getPayee().entrySet().stream().map(Map.Entry::getValue).reduce(0d, (subamt, amt) -> subamt + amt);

            Double eachUserShare = totalAmount / transaction.getExpenseDetails().getShares().size();

            logger.info("2. Started generating owe map");

            Set<String> users = new HashSet<>(transaction.getExpenseDetails().getPayee().keySet());
            transaction.getExpenseDetails().getShares().forEach(user -> users.add(user.getUserId()));
            users.stream().forEach(user -> {
                owesMap.put(user, owesMap.getOrDefault(user, 0d) + transaction.getExpenseDetails().getPayee().getOrDefault(user, 0d) - eachUserShare);
            });

            logger.info("3. Determine dependency map");
            owesMap.entrySet().stream().forEach(userOweEntry -> {
                Set<String> dependentUserList = new HashSet<>();
                if (userOweEntry.getValue() > 0) {
                    owesMap.entrySet().stream().forEach(dependentOweEntry -> {
                        if (!userOweEntry.getKey().equals(dependentOweEntry.getKey())) {
                            if (dependentOweEntry.getValue() < 0) {
                                dependentUserList.add(dependentOweEntry.getKey());
                            }
                        }
                    });
                }
                dependentUserMap.put(userOweEntry.getKey(), dependentUserList);
            });
            logger.info("3. Determine dependency map " + dependentUserMap);
        }

            logger.info("4. settlement map");
            // settlement map
            dependentUserMap.entrySet().forEach(dependentUserEntry -> {
                String oweUser = dependentUserEntry.getKey();
                Set<String> payUsers = dependentUserEntry.getValue();
                payUsers.forEach(user -> {
                    Double userSpendAmt = owesMap.get(user);
                    Double oweAmount = owesMap.get(oweUser);
                    Double amountToPay = Math.abs(userSpendAmt) < oweAmount ? Math.abs(userSpendAmt) : oweAmount;
                    owesMap.put(oweUser, oweAmount - amountToPay);
                    owesMap.put(user, userSpendAmt + amountToPay);

                    if (amountToPay > 0) {
                        if (settlementTransactions.containsKey(user)) {
                            Double revTransacAmount = findSettlementTransactionAmount(settlementTransactions, oweUser,user );
                            if (revTransacAmount > 0) {
                                calculateReverseSettlementLogic(settlementTransactions, oweUser, user, amountToPay, revTransacAmount);
                            } else {
                                Map<String, Double> toTranser = settlementTransactions.get(user);
                                toTranser.put(oweUser,toTranser.getOrDefault(oweUser,0d) +  amountToPay);
                            }

                        } else {
                            Double revTransacAmount = findSettlementTransactionAmount(settlementTransactions, oweUser,user );
                            if (revTransacAmount > 0) {
                                calculateReverseSettlementLogic(settlementTransactions, oweUser, user, amountToPay, revTransacAmount);
                            } else {
                                Map<String, Double> toTranser = new HashMap<>();
                                toTranser.put(oweUser,toTranser.getOrDefault(oweUser,0d) +  amountToPay);

                                settlementTransactions.put(user, toTranser);
                            }
                        }
                    }
                });
            });
            logger.info("settlement map " +  settlementTransactions);


        return (settlementTransactions);
    }

    private void calculateReverseSettlementLogic(Map<String, Map<String, Double>> settlementTransactions, String oweUser, String user, Double amountToPay, Double revTransacAmount) {
        Double settlementAmount = amountToPay > revTransacAmount ? (amountToPay - revTransacAmount) : (revTransacAmount - amountToPay);
        if (amountToPay > revTransacAmount) {
            updateSettlementTransactionAmount(settlementTransactions,  oweUser, user, settlementAmount);
            removeSettlementTransactionAmount(settlementTransactions, user,  oweUser );
        } else {
            updateSettlementTransactionAmount(settlementTransactions, user,  oweUser, settlementAmount);
            removeSettlementTransactionAmount(settlementTransactions, oweUser, user );
        }
    }
    public Double findSettlementTransactionAmount(Map<String,Map<String,Double>> settlementTransactions, String payeeUser, String paidUser) {
        if (settlementTransactions.containsKey(payeeUser) && settlementTransactions.get(payeeUser).containsKey(paidUser)) {
            return settlementTransactions.get(payeeUser).get(paidUser);
        }
        return 0d;
    }

    public void updateSettlementTransactionAmount(Map<String,Map<String,Double>> settlementTransactions, String payeeUser, String paidUser, Double amount) {
        if (settlementTransactions.containsKey(payeeUser) && settlementTransactions.get(payeeUser).containsKey(paidUser)) {
            settlementTransactions.get(payeeUser).put(paidUser, amount);
        }
    }
    public void removeSettlementTransactionAmount(Map<String,Map<String,Double>> settlementTransactions, String payeeUser, String paidUser) {
        if (settlementTransactions.containsKey(payeeUser) && settlementTransactions.get(payeeUser).containsKey(paidUser)) {
            settlementTransactions.get(payeeUser).remove(paidUser);
        }
    }
}
