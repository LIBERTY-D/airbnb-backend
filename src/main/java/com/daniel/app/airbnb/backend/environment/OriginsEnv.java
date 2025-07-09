package com.daniel.app.airbnb.backend.environment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@ConfigurationProperties(prefix = "custom.cors")
@Getter
@Setter
public class OriginsEnv{
    private String  origins;
    public List<String> allowedWebsites() {
        return Arrays.stream(origins.split(",")).map(String::trim).collect(Collectors.toList());
    }

}