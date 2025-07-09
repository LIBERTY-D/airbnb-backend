package com.daniel.app.airbnb.backend.dto;


import com.daniel.app.airbnb.backend.annotations.ValidatingBookingDates;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ValidatingBookingDates()
public class BookingDTO {

    @NotNull(message = "Listing ID is required")
    private Long listingId;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private Date checkIn;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private Date checkOut;
    @NotNull(message = "number of quests are required")
    private Integer  guests;
}
