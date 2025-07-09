package com.daniel.app.airbnb.backend.annotations;

import com.daniel.app.airbnb.backend.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Password and confirm password do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
