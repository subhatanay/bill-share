package com.subadev.billshare.groupbillshare.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<SettlementDetails, String> {

    private static Logger logger = LoggerFactory.getLogger(HashMapConverter.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(SettlementDetails customerInfo) {

        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return customerInfoJson;
    }

    @Override
    public SettlementDetails convertToEntityAttribute(String customerInfoJSON) {

        SettlementDetails customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(customerInfoJSON, new TypeReference<SettlementDetails>() {});
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return customerInfo;
    }
}