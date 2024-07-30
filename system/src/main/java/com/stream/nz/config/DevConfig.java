package com.stream.nz.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "dev")
@Configuration
public class DevConfig {
}
