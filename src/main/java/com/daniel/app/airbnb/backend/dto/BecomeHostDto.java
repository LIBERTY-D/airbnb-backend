package com.daniel.app.airbnb.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BecomeHostDto {
    @Email(message = "email must valid email")
    @NotBlank(message = "email is required ") String email;
}
