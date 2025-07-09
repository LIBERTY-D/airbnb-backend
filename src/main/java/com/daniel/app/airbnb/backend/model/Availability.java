package com.daniel.app.airbnb.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "availability")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availabilityId;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private Date endDate;
}