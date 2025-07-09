package com.daniel.app.airbnb.backend.exception;


import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<Object>> handleException(Exception exp) {
        log.error("[SERVER ERROR] : {}", exp.getMessage());
        var messageExp = exp.getMessage();
        HttpResponse<Object> httpResponse = HttpResponse.builder().build();
        if (messageExp.contains("Required request body is missing")) {
            httpResponse.setMessage("Please Put the Required Body");
            httpResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            httpResponse.setMessage(exp.getMessage());
            httpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        httpResponse.setTimeStamp(LocalDateTime.now());
        return ResponseEntity.internalServerError().body(httpResponse);
    }


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse<Object>> handleLockedException(LockedException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());

        return new ResponseEntity<>(ResponseUtil.getResponse(null, null,
                "account locked. Please verify or contact our team",
                HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());
        Map<String, String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put("class-error", error.getDefaultMessage());//annotation on class level
            }
        });
        return ResponseEntity.badRequest().body(ResponseUtil.getResponse(null, errors, "field errors", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NoSuchUserExp.class)
    public ResponseEntity<HttpResponse<Object>> handleNullEntityException(NoSuchUserExp exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());

        return ResponseEntity.badRequest().body(ResponseUtil.getResponse(null, null, exp.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ParseDateException.class)
    public ResponseEntity<HttpResponse<Object>> handleParseDateException(ParseDateException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());
        return ResponseEntity.badRequest().body(ResponseUtil.getResponse(null, null, exp.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<HttpResponse<Object>> handleBookingException(BookingException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());
        return ResponseEntity.badRequest().body(ResponseUtil.getResponse(null, null, exp.getMessage(), HttpStatus.BAD_REQUEST));
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());

        return new ResponseEntity<>(ResponseUtil.getResponse(null, null, exp.getMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AirBnbListingException.class)
    public ResponseEntity<HttpResponse<Object>> handleAirBnbListingException(AirBnbListingException exp) {
        log.error("[CLIENT ERROR] : {}", exp.getMessage());

        return new ResponseEntity<>(ResponseUtil.getResponse(null, null,
                exp.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException exp) {

        log.error("[CLIENT ERROR] : {}", exp.getMessage());
        var message = exp.getMessage();
        if (message.contains("Key (email)")) {
            message = "Email taken";
        }
        return ResponseEntity.badRequest().body(ResponseUtil.getResponse(null, null, message, HttpStatus.BAD_REQUEST));
    }


}
