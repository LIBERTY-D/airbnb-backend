package com.daniel.app.airbnb.backend.validators;

import com.daniel.app.airbnb.backend.annotations.ValidatingBookingDates;
import com.daniel.app.airbnb.backend.dto.BookingDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingDateValidator implements ConstraintValidator <ValidatingBookingDates, BookingDTO> {
    @Override
    public boolean isValid(BookingDTO value, ConstraintValidatorContext context) {
        if(value.getCheckOut()==null || value.getCheckIn()==null){
            return  true; // @NotNull will handle this
        }
        return  value.getCheckIn().before(value.getCheckOut());
    }
}
