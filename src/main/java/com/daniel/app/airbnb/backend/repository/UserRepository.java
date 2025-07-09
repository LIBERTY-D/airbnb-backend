package com.daniel.app.airbnb.backend.repository;

import com.daniel.app.airbnb.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.listings l
            LEFT JOIN FETCH l.location
            LEFT JOIN FETCH l.images i
            LEFT JOIN FETCH i.gallery
            LEFT JOIN FETCH l.host
            LEFT JOIN FETCH l.features
            LEFT JOIN FETCH l.pricing
            LEFT JOIN FETCH l.availability
            WHERE u.email = :email
            """)
    Optional<User> findByEmailWithFullListings(@Param("email") String email);

    Optional<User> findUserByRegisterToken(String token);
}
