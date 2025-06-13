package com.desmond.gadgetstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GadgetStoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GadgetStoreApplication.class, args);
    }
    
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GadgetStoreApplication.class);
    }


}
