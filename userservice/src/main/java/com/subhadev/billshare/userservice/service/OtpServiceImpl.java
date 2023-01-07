package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dao.OtpRepository;
import com.subhadev.billshare.userservice.entity.OtpEntity;
import com.subhadev.billshare.userservice.helpers.OtpGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {
    private OtpRepository otpRepository;

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }
    @Override
    public String generateAndStoreTOPT(String userId) {
        String otp = OtpGenerator.generateOTP();

        OtpEntity otpEntity = OtpEntity.builder().id(userId).otp(otp).build();
        this.otpRepository.save(otpEntity);
        return otp;
    }

    @Override
    public boolean verifyTOPT(String userId, String totp) {
        Optional<OtpEntity> otpEntity = this.otpRepository.findById(userId);
        if (otpEntity.isPresent()) {
            this.otpRepository.delete(otpEntity.get());
            return totp.equals(otpEntity.get().getOtp());
        }
        return false;
    }
}
