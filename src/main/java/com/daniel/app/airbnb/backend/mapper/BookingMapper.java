package com.daniel.app.airbnb.backend.mapper;


import com.daniel.app.airbnb.backend.dto.BookingResponseDTO;
import com.daniel.app.airbnb.backend.model.Booking;
import com.daniel.app.airbnb.backend.model.Location;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
        Location loc = booking.getListing().getLocation();
        String location = String.format("%s, %s, %s", loc.getCountry(),
                loc.getState(), loc.getCity());
        return new BookingResponseDTO(
                booking.getId(),
                booking.getListing().getId(),
                booking.getUser().getName(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalPrice(),
                location,
                booking.getListing().getTitle(),
                booking.getUser().getEmail(),
                booking.getListing().getImages().getMain(),
                booking.getNumberOfGuests()

        );
    }
}
