package com.daniel.app.airbnb.backend.environment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom.admin")
@Getter
@Setter
public class AdminEnv {
    private  String appEmail;
    private  String appPassword;
    private  String appUsername;
}
