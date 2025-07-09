package com.daniel.app.airbnb.backend.repository;


import com.daniel.app.airbnb.backend.model.AirBnbListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirBnbRepository extends JpaRepository<AirBnbListing, Long> {
}
