package com.daniel.app.airbnb.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    @NotBlank(message = "fullName required")
    private String fullName;
    @NotBlank(message = "email required")
    @Email(message = "email must be a valid email")
    private String email;
    @NotBlank(message = "message required")
    private String message;
}
