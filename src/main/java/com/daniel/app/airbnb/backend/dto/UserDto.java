package com.daniel.app.airbnb.backend.dto;

import com.daniel.app.airbnb.backend.annotations.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@PasswordMatches()
public record UserDto(@NotBlank(message = "name and surname required") String name ,
                      @NotBlank(message = "email is required ") String email,
                      @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank(message = "password is required")String password,
                      @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank(message = "confirm required")String   confirmPassword,
                      @NotNull(message = "year your joined is required ") Integer joinedYear )  {
}
