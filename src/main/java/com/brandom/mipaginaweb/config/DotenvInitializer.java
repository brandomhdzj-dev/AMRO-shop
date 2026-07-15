package com.brandom.mipaginaweb.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            setIfPresent("GOOGLE_CLIENT_ID", dotenv);
            setIfPresent("GOOGLE_CLIENT_SECRET", dotenv);
            setIfPresent("CLOUDINARY_CLOUD_NAME", dotenv);
            setIfPresent("CLOUDINARY_API_KEY", dotenv);
            setIfPresent("CLOUDINARY_API_SECRET", dotenv);
        } catch (Exception e) {
            // Ignore: .env file not present (e.g. in production)
        }
    }

    private void setIfPresent(String key, Dotenv dotenv) {
        String value = dotenv.get(key);
        if (value != null && !value.isEmpty()) {
            System.setProperty(key, value);
        }
    }
}