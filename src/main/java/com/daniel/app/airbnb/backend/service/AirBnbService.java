package com.daniel.app.airbnb.backend.service;


import com.daniel.app.airbnb.backend.dto.AirBnbResponseDto;
import com.daniel.app.airbnb.backend.dto.ListingDTO;
import com.daniel.app.airbnb.backend.dto.PartialListingUpdateDTO;
import com.daniel.app.airbnb.backend.exception.AirBnbListingException;
import com.daniel.app.airbnb.backend.mapper.ListingMapper;
import com.daniel.app.airbnb.backend.model.AirBnbListing;
import com.daniel.app.airbnb.backend.repository.AirBnbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AirBnbService {

    private final AirBnbRepository airBnbRepository;
    private final ListingMapper listingMapper;

    public List<AirBnbResponseDto> GetAllListings() {

        return airBnbRepository.findAll().stream()
                .map(listingMapper::toAirBnbResponseDto).toList();
    }

    public AirBnbResponseDto GetListing(Long id) {
        return Stream.of(findAirBnbById(id)).map(listingMapper::toAirBnbResponseDto).findFirst().get();
    }

    @Transactional
    public AirBnbResponseDto createList(ListingDTO listingDTO) {

        AirBnbListing airBnbListing = listingMapper.mapToEntity(listingDTO);
        var saved = airBnbRepository.save(airBnbListing);
        return Stream.of(saved).map(listingMapper::toAirBnbResponseDto).findFirst().get();
    }

    public void DeleteListing(Long id) {
        airBnbRepository.deleteById(id);
    }

    public AirBnbResponseDto updateListing(Long id, PartialListingUpdateDTO partialListingUpdateDTO) {
        return Stream.of(listingMapper.partialUpdate(id,
                partialListingUpdateDTO)).map(listingMapper::toAirBnbResponseDto).findFirst().get();
    }

    private AirBnbListing findAirBnbById(Long id) {

        Optional<AirBnbListing> optionalAirBnbListing = airBnbRepository.findById(id);
        if (optionalAirBnbListing.isEmpty()) {
            throw new AirBnbListingException("AirBnb not found");
        }
        return optionalAirBnbListing.get();
    }
}
