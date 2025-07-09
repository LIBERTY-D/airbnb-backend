package com.daniel.app.airbnb.backend.dto;

import com.daniel.app.airbnb.backend.model.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
public  class AirBnbResponseDto {
    private Long id;
    private String title;
    private Long userId;
    private Location location;
    private List<Booking> bookings = new ArrayList<>();
    private Images images;
    private Host host;
    private Features features;
    private String description;
    private List<String> amenities;
    private Pricing pricing;
    private Availability availability;

}