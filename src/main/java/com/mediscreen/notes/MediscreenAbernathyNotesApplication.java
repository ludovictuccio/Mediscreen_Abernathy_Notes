package com.mediscreen.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mediscreen.notes.util.ConstraintsValidator;

@SpringBootApplication
public class MediscreenAbernathyNotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediscreenAbernathyNotesApplication.class, args);
    }

    @Bean
    public ConstraintsValidator getConstraintsValidator() {
        return new ConstraintsValidator();
    }

}
