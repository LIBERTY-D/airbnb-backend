package com.daniel.app.airbnb.backend.service.email;


import java.util.Map;

public interface EmailService {

    void sendEmail(Map<String, Object> payload,String template);
}
