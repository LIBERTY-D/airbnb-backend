package com.daniel.app.airbnb.backend.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "pexelsClient", url = "https://api.pexels.com/v1")
public interface PexelsClient {
    @GetMapping("/search")
    Map<String, Object> searchPhotos(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam("query") String query,
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page
    );
}
