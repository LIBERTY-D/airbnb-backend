package com.daniel.app.airbnb.backend.environment;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom.jwt")
@Getter
@Setter
public class JwtEnv {
    private  Long jwtAccessExp;
    private Long jwtRefreshExp;
    private String jwtSecret;

}
