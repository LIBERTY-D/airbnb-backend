package com.daniel.app.airbnb.backend.annotations;

import com.daniel.app.airbnb.backend.validators.BookingDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingDateValidator.class)
@Documented
public @interface ValidatingBookingDates {
    String message() default "Check-in must be before check-out";

    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
