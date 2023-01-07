package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.exception.ValidationException;

public interface Validation {

    void validate() throws ValidationException;

}
