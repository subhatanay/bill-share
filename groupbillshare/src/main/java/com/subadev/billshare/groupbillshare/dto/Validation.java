package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.exception.ValidationException;

public interface Validation {

    void validate() throws ValidationException;

}
