package ru.mephi.bakinaa.lab3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.mephi.bakinaa.lab3.utils.convertes.RelationResponseCsvHttpMessageConverter;
import ru.mephi.bakinaa.lab3.utils.convertes.SimpleObjResponseCsvHttpMessageConverter;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new RelationResponseCsvHttpMessageConverter());
        messageConverters.add(new SimpleObjResponseCsvHttpMessageConverter());
    }
}
