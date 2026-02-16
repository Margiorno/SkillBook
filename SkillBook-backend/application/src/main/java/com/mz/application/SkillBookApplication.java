package com.mz.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mz")
public class SkillBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillBookApplication.class, args);
    }
}
