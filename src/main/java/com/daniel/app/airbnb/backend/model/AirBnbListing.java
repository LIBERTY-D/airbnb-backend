package com.daniel.app.airbnb.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airbnb_listing")

public class AirBnbListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    @JsonManagedReference()
    private List<Booking> bookings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "images_id")
    private Images images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feature_id")
    private Features features;

    @Column(columnDefinition = "TEXT")
    private String description;

    private List<String> amenities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pricing_id")
    private Pricing pricing;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "availability_id")
    private Availability availability;

}