package com.KapybaraWeb.kapyweb.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap();
        config.put("cloud_name", "kapibala");
        config.put("api_key", "879472466156866");
        config.put("api_secret", "CaCqEd2_FxZ4TwhSLpk5dW8c1ys");
        return new Cloudinary(config);
    }
}
