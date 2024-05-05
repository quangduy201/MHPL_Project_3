package com.example.project_3.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    public static final long MAX_AGE_SECS = 3600;
    private final Environment env;

    @Autowired
    public WebMvcConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(getAllowedOrigins())
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }

    private String[] getAllowedOrigins() {
        String allowedOrigins = env.getProperty("app.cors.allowed.origins");
        if (allowedOrigins == null)
            return new String[]{};
        return allowedOrigins.split("\\s*,+\\s*");
    }

}
