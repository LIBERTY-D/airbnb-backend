package com.daniel.app.airbnb.backend.dto;

import jakarta.validation.constraints.NotBlank;


public record LoginDto(@NotBlank(message = "email is required ") String email,
                       @NotBlank(message = "password is required") String password
) {
}
