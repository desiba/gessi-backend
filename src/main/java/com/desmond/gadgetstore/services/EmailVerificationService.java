package com.desmond.gadgetstore.services;

import java.util.UUID;

import com.desmond.gadgetstore.entities.UserEntity;

public interface EmailVerificationService {
	UserEntity verifyEmailOtp();
	void sendEmailVerificationOtp(final UUID userId, final String email);
}
