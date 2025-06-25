package com.desmond.gadgetstore.payload.response;

public class ResponseUtil {
	
	public static <T> ApiResponse<T> success(String message, T data, Object metadata) {
		return ApiResponse.<T>builder()
				.status("success")
				.message(message)
				.data(data)
				.metadata(metadata)
				.build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
    	return ApiResponse.<T>builder()
				.status("error")
				.message(message)
				.data(data)
				.metadata(null)
				.build();
    }

}
