package com.daniel.app.airbnb.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "pricing")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceId;
    private double perNight;
    private double rating;
}