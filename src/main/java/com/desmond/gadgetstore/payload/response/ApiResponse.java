package com.desmond.gadgetstore.payload.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.desmond.gadgetstore.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String status;
    private String message;
    private T data;
    private Object metadata;
	
}
