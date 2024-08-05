package com.nishantgupta.JMA2.Response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private String message;
	private String status;
	private Instant timestamp;
	
	
    public ErrorResponse(String message, String status, Instant timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

}
