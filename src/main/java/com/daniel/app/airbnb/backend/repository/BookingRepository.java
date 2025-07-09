package com.daniel.app.airbnb.backend.repository;


import com.daniel.app.airbnb.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByListingId(Long listingId);
}
