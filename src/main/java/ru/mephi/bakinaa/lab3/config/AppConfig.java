package ru.mephi.bakinaa.lab3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public record AppConfig(
        String databaseLocation,
        boolean saveToFile
) {
}
