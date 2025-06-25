package com.desmond.gadgetstore.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

public class TokenRefreshException extends RuntimeException{
	 public TokenRefreshException(String token, String message) {
		    super(String.format("Failed for [%s]: %s", token, message));
     }
}
