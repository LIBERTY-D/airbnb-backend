package com.daniel.app.airbnb.backend.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GitHubClient {
    @GetMapping("/user/emails")
    List<GitHubEmail> getUserEmails(@RequestHeader("Authorization") String bearerToken);
}
