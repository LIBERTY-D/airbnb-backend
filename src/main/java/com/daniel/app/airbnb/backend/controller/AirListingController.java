package com.daniel.app.airbnb.backend.controller;


import com.daniel.app.airbnb.backend.dto.AirBnbResponseDto;
import com.daniel.app.airbnb.backend.dto.ListingDTO;
import com.daniel.app.airbnb.backend.dto.PartialListingUpdateDTO;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.AirBnbService;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AirListingController extends BaseController {


    private final AirBnbService airBnbService;

    @GetMapping("listings")
    public ResponseEntity<HttpResponse<List<AirBnbResponseDto>>> GetAllListings() {
        var data = airBnbService.GetAllListings();
        return ResponseEntity.ok(ResponseUtil.getResponse(data, null, "fetched" + " airbnbs", HttpStatus.OK));
    }


    @GetMapping("listings/{listingId}")
    public ResponseEntity<HttpResponse<AirBnbResponseDto>> GetListing(@PathVariable("listingId") Long id) {
        var data = airBnbService.GetListing(id);
        return ResponseEntity.ok(ResponseUtil.getResponse(data, null, "fetched" + " airbnb", HttpStatus.OK));
    }

    @PostMapping("listings")
    public ResponseEntity<HttpResponse<AirBnbResponseDto>> createList(
            @RequestPart(value = "ListingDTO") ListingDTO listingDTO,
            @RequestPart(value = "gallery") MultipartFile[] galleryImages,
            @RequestPart(value = "main") MultipartFile mainImage) throws IOException {
        if (galleryImages != null) {
            List<String> galleryList = new ArrayList<>();
            for (MultipartFile f : galleryImages) {
                byte[] bytes = f.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                galleryList.add(base64);
            }
            listingDTO.setGallery(galleryList);
        }

        if (mainImage != null) {
            String base64Main = Base64.getEncoder().encodeToString(mainImage.getBytes());
            listingDTO.setMainImage(base64Main);
        }

        var data = airBnbService.createList(listingDTO);
        return ResponseEntity.ok(ResponseUtil.getResponse(data, null, "created" + " airbnb", HttpStatus.OK));
    }


    @DeleteMapping("listings/{listingId}")
    public ResponseEntity<HttpResponse<Object>> DeleteListing(@PathVariable("listingId") Long id) {
        airBnbService.DeleteListing(id);
        return ResponseEntity.ok(ResponseUtil.getResponse(null, null, "deleted" + " airbnb", HttpStatus.OK));
    }

    @PatchMapping(value = "listings/{listingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse<AirBnbResponseDto>> updateListing(
            @PathVariable("listingId") Long id,
            @RequestPart(value = "partialListing", required = false)
            PartialListingUpdateDTO partialListingUpdateDTO,
            @RequestPart(value = "gallery", required = false)
            MultipartFile[] galleryImages,
            @RequestPart(value = "main", required = false) MultipartFile mainImage

    ) throws IOException {

        if (galleryImages != null) {
            List<String> galleryList = new ArrayList<>();
            for (MultipartFile f : galleryImages) {
                byte[] bytes = f.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                galleryList.add(base64);
            }
            partialListingUpdateDTO.setGallery(galleryList);
        }

        if (mainImage != null) {
            String base64Main = Base64.getEncoder().encodeToString(mainImage.getBytes());
            partialListingUpdateDTO.setMainImage(base64Main);
        }

        var data = airBnbService.updateListing(id, partialListingUpdateDTO);
        return ResponseEntity.ok(ResponseUtil.getResponse(data, null, "updated airbnb", HttpStatus.OK));
    }


}
