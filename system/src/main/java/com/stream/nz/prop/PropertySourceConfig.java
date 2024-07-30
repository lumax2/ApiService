package com.stream.nz.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertySourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }
}
