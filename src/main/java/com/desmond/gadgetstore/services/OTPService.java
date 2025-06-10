package com.desmond.gadgetstore.services;

public interface OTPService {
    String generateOTP(String phoneNumber);

    boolean validateOTP(String phoneNumber, String otp);
}
