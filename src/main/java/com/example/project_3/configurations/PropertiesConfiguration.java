package com.example.project_3.configurations;

import com.example.project_3.utils.Resource;
import com.example.project_3.utils.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;

@Configuration
public class PropertiesConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        Path path = Resource.getPathFromResource(Settings.APP_FILE, false);
        configurer.setLocation(new FileSystemResource(path));
        configurer.setIgnoreResourceNotFound(false);
        return configurer;
    }
}
