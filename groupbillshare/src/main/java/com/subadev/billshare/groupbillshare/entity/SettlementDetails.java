package com.subadev.billshare.groupbillshare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDetails implements Serializable {
    Map<String, Double> payee;
    List<Share> shares;

}
