package com.daniel.app.airbnb.backend.dto;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class ListingDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Main image URL is required")
    private String mainImage;

    @NotNull(message = "Gallery must not be null")
    @Size(min = 1, message = "At least one gallery image is required")
    private List<@NotNull(message = "Image data must not be null") String> gallery;


    @NotBlank(message = "Host name is required")
    private String hostName;

    @Min(value = 2000, message = "Joined year must be after 2000")
    private int hostJoinedYear;

    private boolean isSuperhost;

    @Min(value = 1, message = "There must be at least 1 bedroom")
    private int bedrooms;

    @Min(value = 1, message = "There must be at least 1 bathroom")
    private int bathrooms;

    @Min(value = 1, message = "There must be at least 1 guest")
    private int guests;

    private boolean wifi;
    private boolean fireplace;
    private boolean mountainView;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amenities must not be null")
    @Size(min = 1, message = "At least one amenity is required")
    private List<@NotBlank(message = "Amenity must not be blank") String> amenities;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private double perNight;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5")
    private double rating;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "End date is required")
    private String endDate;
}
