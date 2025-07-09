package com.daniel.app.airbnb.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PartialListingUpdateDTO {
    private String title;
    private String city;
    private String state;
    private String country;
    private String mainImage;
    private List<String> gallery;
    private String hostName;
    private Integer hostJoinedYear;
    private Boolean isSuperhost;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer guests;
    private Boolean wifi;
    private Boolean fireplace;
    private Boolean mountainView;
    private String description;
    private List<String> amenities;
    private Double perNight;
    private Double rating;
    private String startDate;
    private String endDate;
}
