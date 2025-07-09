package com.daniel.app.airbnb.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class Features {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  featureId;
    private int bedrooms;
    private int bathrooms;
    private int guests;
    private boolean wifi;
    private boolean fireplace;
    private boolean mountainView;
}