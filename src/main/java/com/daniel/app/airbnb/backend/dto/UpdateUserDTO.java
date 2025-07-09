package com.daniel.app.airbnb.backend.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String name;

    private String email;

    private String phone;

    private Integer joinedYear;

    private boolean superhost;

    private String password;
}
