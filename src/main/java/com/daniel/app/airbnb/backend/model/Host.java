package com.daniel.app.airbnb.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "host")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostId;
    private String name;
    private int joinedYear;
    private boolean isSuperhost;
}
