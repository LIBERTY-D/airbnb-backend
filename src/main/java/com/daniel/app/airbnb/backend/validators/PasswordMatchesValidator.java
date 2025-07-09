package com.daniel.app.airbnb.backend.validators;


import com.daniel.app.airbnb.backend.dto.UserDto;
import com.daniel.app.airbnb.backend.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {

    @Override
    public boolean isValid(UserDto dto, ConstraintValidatorContext context) {
        if (dto.password() == null || dto.confirmPassword() == null) return false;
        return dto.password().equals(dto.confirmPassword());
    }
}
