package com.daniel.app.airbnb.backend.controller;


import com.daniel.app.airbnb.backend.dto.BookingDTO;
import com.daniel.app.airbnb.backend.dto.BookingResponseDTO;
import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.BookingService;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController extends BaseController {
    private final BookingService bookingService;

    @PostMapping("bookings")
    public ResponseEntity<HttpResponse<BookingResponseDTO>> bookListing(@Valid @RequestBody BookingDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookingResponseDTO booking = bookingService.createBooking(dto, user);
        return ResponseEntity.ok(ResponseUtil.getResponse(booking, null,
                "successfully booked", HttpStatus.OK));
    }

    @GetMapping("bookings")
    public ResponseEntity<HttpResponse<List<BookingResponseDTO>>> getBookings() {

        var bookings = bookingService.findAllBookings();
        return ResponseEntity.ok(ResponseUtil.getResponse(bookings, null,
                "fetched " +
                        "all bookings successfully", HttpStatus.OK));
    }

    @GetMapping("bookings/{id}")
    public ResponseEntity<HttpResponse<BookingResponseDTO>> getBooking(@PathVariable Long id) {
        var booking = bookingService.findOneBooking(id);
        return ResponseEntity.ok(ResponseUtil.getResponse(booking, null,
                "fetched booking successfully", HttpStatus.OK));
    }


}
