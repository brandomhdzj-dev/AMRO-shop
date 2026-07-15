package com.brandom.mipaginaweb.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Autowired
    private Environment env;

    @Bean
    public Cloudinary cloudinary() {
        String cloudName = env.getProperty("CLOUDINARY_CLOUD_NAME", "");
        String apiKey = env.getProperty("CLOUDINARY_API_KEY", "");
        String apiSecret = env.getProperty("CLOUDINARY_API_SECRET", "");

        System.out.println("CLOUDINARY DEBUG: cloud_name=[" + cloudName + "] api_key=[" + apiKey + "] api_secret=[" + apiSecret + "]");

        return new Cloudinary(Map.of(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
    }
}
