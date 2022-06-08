package com.switchkit.incidentreport.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;


public record ExceptionResponse(HttpStatus httpStatus, String message) {
}