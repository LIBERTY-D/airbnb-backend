package com.daniel.app.airbnb.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;
    private Long listingId;
    private String username;
    private Date checkIn;
    private Date checkOut;
    private double totalPrice;
    private  String location;
    private  String houseName;
    private  String email;
    private  String mainImage;
    private  int numberOfGuests;

}
