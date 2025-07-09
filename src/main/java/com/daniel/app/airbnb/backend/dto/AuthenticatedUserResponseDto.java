package com.daniel.app.airbnb.backend.dto;

import com.daniel.app.airbnb.backend.model.AirBnbListing;
import com.daniel.app.airbnb.backend.model.enums.Provider;
import com.daniel.app.airbnb.backend.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class AuthenticatedUserResponseDto {
    private Long userId;
    private boolean verified;
    private String registerToken;
    private Provider provider;
    private String name;
    private String email;
    private String phone;
    private Set<Role> roles;
    private Integer joinedYear;
    private boolean isSuperhost;
    @JsonIgnoreProperties("bookings")
    private Set<AirBnbListing> listings;
    private List<BookingResponseDTO> bookings;
}
