package com.daniel.app.airbnb.backend.util;

import com.daniel.app.airbnb.backend.dto.BookingResponseDTO;
import com.daniel.app.airbnb.backend.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MailUtil {

    public static Map<String, Object> getPayload(BookingResponseDTO bookingResponseDTO, String actionUrl) {
        Map<String, Object> model = new HashMap<>();
        model.put("subject", "Booking Confirmed");
        model.put("to", bookingResponseDTO.getEmail());
        model.put("title", "Your Stay is Booked!");
        model.put("username", bookingResponseDTO.getUsername());
        model.put("houseName", bookingResponseDTO.getHouseName());
        model.put("location", bookingResponseDTO.getLocation());
        model.put("checkIn", bookingResponseDTO.getCheckIn());
        model.put("checkOut", bookingResponseDTO.getCheckOut());
        model.put("price", "$" + bookingResponseDTO.getTotalPrice());
        model.put("buttonText", "View Your Booking");
        int year = LocalDate.now().getYear();
        model.put("actionUrl", actionUrl);
        model.put("footerText", "Cozy Bay © " + year);

        return model;
    }

    public static Map<String, Object> getPayload(User user, String actionUrl) {
        Map<String, Object> model = new HashMap<>();
        model.put("to", user.getEmail());
        model.put("subject", "Account Created");
        model.put("title", "Creating Account Confirmed!");
        model.put("username", user.getName());
        model.put("buttonText", "Verify Email");
        model.put("actionUrl", actionUrl);
        int year = LocalDate.now().getYear();
        model.put("footerText", "Cozy Bay © " + year);

        return model;
    }

    public static Map<String, Object> contactPayload(
            String to, String username) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("subject", "Thank you for contacting our team");
        payload.put("to", to);
        payload.put("title", "");
        payload.put("username", username);
        payload.put("message", "<p> Thank you for contacting us. Our team " +
                "will respond to you shortly!</p>");
        payload.put("footerText", "If you did’t contact, you can safely " +
                "ignore this email.");
        return payload;
    }

}
