package com.desmond.gadgetstore.payload.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({
	"success",
	"message"
})
public class ApiResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("success")
	private Boolean success;
	@JsonProperty("message")
	private String message;
	@JsonProperty("data")
	private Object data;
	@JsonProperty("token")
	private String token;
	
	@JsonIgnore
	private HttpStatus status;
	
	public ApiResponse() {}
	
	public ApiResponse(Boolean success, String message, Object data, String token) {
		this.success = success;
		this.message = message;
		this.data = data;
		this.token = token;
	}
	
	public ApiResponse(Boolean success, String message, HttpStatus httpStatus) {
		this.success = success;
		this.message = message;
		this.status = httpStatus;
	}
	
}
