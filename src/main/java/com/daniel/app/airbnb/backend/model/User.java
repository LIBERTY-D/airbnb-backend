package com.daniel.app.airbnb.backend.model;

import com.daniel.app.airbnb.backend.model.enums.Provider;
import com.daniel.app.airbnb.backend.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "AppUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private boolean verified;
    private String registerToken;

    @Enumerated(EnumType.STRING)
    private Provider provider; // GOOGLE, GITHUB, LOCAL

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    //    @Enumerated(EnumType.STRING)
    //  private Role role = Role.ROLE_USER;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_USER));


    private Integer joinedYear;

    private boolean isSuperhost;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval =
            true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties({"bookings", "user"})
    private Set<AirBnbListing> listings = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =
            FetchType.EAGER)
    @JsonManagedReference()
    @JsonIgnoreProperties("user")
    private List<Booking> bookings = new ArrayList<>();

    public User() {
    }


    public User(String name, String email, String password, Integer joinedYear,
                boolean isSuperhost, Provider provider) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.joinedYear = joinedYear;
        this.isSuperhost = isSuperhost;
        this.provider = provider;
    }

    public User(String name, String email, Integer joinedYear, boolean isSuperhost,
                Provider provider) {
        this.name = name;
        this.email = email;
        this.joinedYear = joinedYear;
        this.isSuperhost = isSuperhost;
        this.provider = provider;
    }

    public User(String name, String email, Integer joinedYear, Provider provider) {
        this.name = name;
        this.email = email;
        this.joinedYear = joinedYear;
        this.provider = provider;

    }

    public User(String name, String email, String password, Integer joinedYear,
                Provider provider) {
        this.name = name;
        this.email = email;
        this.joinedYear = joinedYear;
        this.provider = provider;
        this.password = password;

    }
}
