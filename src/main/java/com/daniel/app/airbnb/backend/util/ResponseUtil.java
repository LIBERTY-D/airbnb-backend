package com.daniel.app.airbnb.backend.util;

import com.daniel.app.airbnb.backend.res.HttpResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseUtil {

    public static  <T> HttpResponse<T> getResponse(T typeData, Map<?, ?> err,
                                           String message, HttpStatus httpStatus) {
        HttpResponse<T> response = HttpResponse.<T>builder().build();

        if (typeData != null) {
            response.setData(typeData);
        }
        if (err != null) {
            response.setErrors(err);
        }
        response.setStatus(httpStatus);
        response.setStatusCode(httpStatus.value());
        response.setMessage(message);
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

}
