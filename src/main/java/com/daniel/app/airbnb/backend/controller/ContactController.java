package com.daniel.app.airbnb.backend.controller;

import com.daniel.app.airbnb.backend.dto.ContactDto;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.ContactServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ContactController extends BaseController {


    private final ContactServiceImp contactServiceImp;

    @PostMapping("contact")
    public ResponseEntity<HttpResponse<Object>> contactUs(@Valid @RequestBody() ContactDto contactDto) {

        contactServiceImp.ContactUs(contactDto);
        HttpResponse<Object> httpResponse = HttpResponse.builder().build();
        httpResponse.setMessage("Thank you for contacting us");
        httpResponse.setTimeStamp(LocalDateTime.now());
        httpResponse.setStatusCode(HttpStatus.OK.value());
        httpResponse.setStatus(HttpStatus.OK);
        return ResponseEntity.ok(httpResponse);

    }
}
