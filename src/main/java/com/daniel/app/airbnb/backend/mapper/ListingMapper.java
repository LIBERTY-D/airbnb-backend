package com.daniel.app.airbnb.backend.mapper;

import com.daniel.app.airbnb.backend.dto.AirBnbResponseDto;
import com.daniel.app.airbnb.backend.dto.ListingDTO;
import com.daniel.app.airbnb.backend.dto.PartialListingUpdateDTO;
import com.daniel.app.airbnb.backend.exception.AirBnbListingException;
import com.daniel.app.airbnb.backend.exception.NoSuchUserExp;
import com.daniel.app.airbnb.backend.model.*;
import com.daniel.app.airbnb.backend.model.enums.Role;
import com.daniel.app.airbnb.backend.repository.AirBnbRepository;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import com.daniel.app.airbnb.backend.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListingMapper {

    private final AirBnbRepository repository;
    private final UserRepository userRepository;

    public AirBnbListing mapToEntity(ListingDTO dto) {
        AirBnbListing listing = new AirBnbListing();

        listing.setTitle(dto.getTitle());

        Location location = new Location();
        location.setCity(dto.getCity());
        location.setState(dto.getState());
        location.setCountry(dto.getCountry());
        listing.setLocation(location);

        Images images = new Images();
        images.setMain(dto.getMainImage());
        images.setGallery(dto.getGallery());
        listing.setImages(images);

        Host host = new Host();
        host.setName(dto.getHostName());
        host.setJoinedYear(dto.getHostJoinedYear());
        host.setSuperhost(dto.isSuperhost());
        listing.setHost(host);

        Features features = new Features();
        features.setBedrooms(dto.getBedrooms());
        features.setBathrooms(dto.getBathrooms());
        features.setGuests(dto.getGuests());
        features.setWifi(dto.isWifi());
        features.setFireplace(dto.isFireplace());
        features.setMountainView(dto.isMountainView());
        listing.setFeatures(features);

        listing.setDescription(dto.getDescription());
        listing.setAmenities(dto.getAmenities());

        Pricing pricing = new Pricing();
        pricing.setPerNight(dto.getPerNight());
        pricing.setRating(dto.getRating());
        listing.setPricing(pricing);

        Availability availability = new Availability();

        availability.setStartDate(DateUtil.getDateFormat(dto.getStartDate()));
        availability.setEndDate(DateUtil.getDateFormat(dto.getEndDate()));
        listing.setAvailability(availability);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(() -> new NoSuchUserExp("User not found"));
        var newRole = user.getRoles();
        newRole.add(Role.ROLE_HOST);
        user.setRoles(newRole);
        userDb.setSuperhost(true);
        listing.setUser(userDb);
        return listing;
    }

    public AirBnbListing partialUpdate(Long id, PartialListingUpdateDTO dto) {

        AirBnbListing listing = repository.findById(id)
                .orElseThrow(() -> new AirBnbListingException("Listing not found"));

        if (dto.getTitle() != null) listing.setTitle(dto.getTitle());
        if (dto.getCity() != null) {
            if (listing.getLocation() == null)
                listing.setLocation(new Location());
            listing.getLocation().setCity(dto.getCity());
        }
        if (dto.getState() != null)
            listing.getLocation().setState(dto.getState());
        if (dto.getCountry() != null)
            listing.getLocation().setCountry(dto.getCountry());

        if (dto.getMainImage() != null) {
            if (listing.getImages() == null) listing.setImages(new Images());
            listing.getImages().setMain(dto.getMainImage());
        }

        if (dto.getGallery() != null)
            listing.getImages().setGallery(dto.getGallery());

        if (dto.getHostName() != null) {
            if (listing.getHost() == null) listing.setHost(new Host());
            listing.getHost().setName(dto.getHostName());
        }
        if (dto.getHostJoinedYear() != null)
            listing.getHost().setJoinedYear(dto.getHostJoinedYear());
        if (dto.getIsSuperhost() != null)
            listing.getHost().setSuperhost(dto.getIsSuperhost());

        if (dto.getBedrooms() != null)
            listing.getFeatures().setBedrooms(dto.getBedrooms());
        if (dto.getBathrooms() != null)
            listing.getFeatures().setBathrooms(dto.getBathrooms());
        if (dto.getGuests() != null)
            listing.getFeatures().setGuests(dto.getGuests());

        if (dto.getWifi() != null) listing.getFeatures().setWifi(dto.getWifi());
        if (dto.getFireplace() != null)
            listing.getFeatures().setFireplace(dto.getFireplace());
        if (dto.getMountainView() != null)
            listing.getFeatures().setMountainView(dto.getMountainView());

        if (dto.getDescription() != null)
            listing.setDescription(dto.getDescription());
        if (dto.getAmenities() != null)
            listing.setAmenities(dto.getAmenities());

        if (dto.getPerNight() != null)
            listing.getPricing().setPerNight(dto.getPerNight());
        if (dto.getRating() != null)
            listing.getPricing().setRating(dto.getRating());
        if (dto.getStartDate() != null) {
            listing.getAvailability().setStartDate(DateUtil.getDateFormat(dto.getStartDate()));
        }
        if (dto.getEndDate() != null) {
            listing.getAvailability().setEndDate(DateUtil.getDateFormat(dto.getEndDate()));
        }

        return repository.save(listing);
    }

    public AirBnbResponseDto toAirBnbResponseDto(AirBnbListing airBnbListing) {
        if (airBnbListing == null) {
            throw new AirBnbListingException("Listing not found");
        }

        var user = airBnbListing.getUser();
        return new AirBnbResponseDto(airBnbListing.getId(), airBnbListing.getTitle(),
                user.getUserId(),
                airBnbListing.getLocation(), airBnbListing.getBookings(),
                airBnbListing.getImages(), airBnbListing.getHost(),
                airBnbListing.getFeatures(), airBnbListing.getDescription(),
                airBnbListing.getAmenities(), airBnbListing.getPricing(),
                airBnbListing.getAvailability());
    }

}

