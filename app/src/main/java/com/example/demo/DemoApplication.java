package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication {

    @Value("${app.version:unknown}")
    private String version;

    @Value("${HOSTNAME:local}")
    private String hostname;

    @GetMapping("/")
    public String home() {
        return "Java Application Running Successfully";
    }

    @GetMapping("/version")
    public Map<String, String> version() {

        Map<String, String> response = new HashMap<>();

        response.put("application", "java-demo");
        response.put("version", version);
        response.put("pod", hostname);

        return response;
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    public static void main(String[] args) {

        System.out.println("Starting Java Application Release...");

        SpringApplication.run(DemoApplication.class, args);

        System.out.println("Application started successfully.");
    }
}