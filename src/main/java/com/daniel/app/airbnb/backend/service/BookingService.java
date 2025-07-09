package com.daniel.app.airbnb.backend.service;


import com.daniel.app.airbnb.backend.dto.BookingDTO;
import com.daniel.app.airbnb.backend.dto.BookingResponseDTO;
import com.daniel.app.airbnb.backend.exception.BookingException;
import com.daniel.app.airbnb.backend.mapper.BookingMapper;
import com.daniel.app.airbnb.backend.model.AirBnbListing;
import com.daniel.app.airbnb.backend.model.Booking;
import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.repository.AirBnbRepository;
import com.daniel.app.airbnb.backend.repository.BookingRepository;
import com.daniel.app.airbnb.backend.service.email.EmailService;
import com.daniel.app.airbnb.backend.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AirBnbRepository airBnbRepository;
    private final BookingMapper bookingMapper;
    private final EmailService emailService;
    @Value("${custom.redirect-verify")
    private String redirectUrl;
    @Value("${custom.mail.enabled:false}")
    private boolean emailEnabled;

    @Transactional
    public BookingResponseDTO createBooking(BookingDTO bookingDTO, User user) {
        AirBnbListing listing = airBnbRepository.findById(bookingDTO.getListingId())
                .orElseThrow(() -> new BookingException("No such listing to " +
                        "book"));

        // Check availability
        boolean isAvailable = isListingAvailable(listing, bookingDTO.getCheckIn(), bookingDTO.getCheckOut());
        if (!isAvailable) {
            throw new BookingException("Listing not available for selected dates");
        }

        // Calculate total price
        LocalDate start = bookingDTO.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = bookingDTO.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long nights = ChronoUnit.DAYS.between(start, end);
        double price = listing.getPricing().getPerNight() * nights;

        // Save booking
        Booking booking = new Booking();
        booking.setListing(listing);
        booking.setUser(user);
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setTotalPrice(price);
        booking.setConfirmed(true);
        booking.setNumberOfGuests(bookingDTO.getGuests());
        // TODO: in future to process actual payment
        Booking b = bookingRepository.save(booking);
        BookingResponseDTO bookRes = bookingMapper.mapToBookingResponseDTO(b);
        // send email

        if (emailEnabled) {
            emailService.sendEmail(MailUtil.getPayload(bookRes, redirectUrl + "/bookings"),
                    "book" +
                            "-confirmation.html");
        }
        return bookRes;

    }

    public BookingResponseDTO findOneBooking(Long id) {

        var booking = findById(id);
        return bookingMapper.mapToBookingResponseDTO(booking);
    }

    public List<BookingResponseDTO> findAllBookings() {

        return bookingRepository.findAll().stream().map(bookingMapper::mapToBookingResponseDTO).toList();
    }

    private boolean isListingAvailable(AirBnbListing listing, Date start,
                                       Date end) {
        List<Booking> bookings = bookingRepository.findByListingId(listing.getId());

        for (Booking booking : bookings) {
            boolean overlaps = !booking.getCheckOut().before(start) && !booking.getCheckIn().after(end);
            if (overlaps) {
                return false; // Dates overlap with existing booking
            }
        }
        return true;
    }


    private Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingException("no booking with such id"));
    }

    ;


}
