package com.daniel.app.airbnb.backend.service;


import com.daniel.app.airbnb.backend.dto.ContactDto;
import com.daniel.app.airbnb.backend.service.email.EmailService;
import com.daniel.app.airbnb.backend.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImp {
    private final EmailService emailService;

    public void ContactUs(ContactDto contactDto) {

        emailService.sendEmail(MailUtil.contactPayload(contactDto.getEmail(), contactDto.getFullName()), "contact" +
                "-us.html");

    }
}
