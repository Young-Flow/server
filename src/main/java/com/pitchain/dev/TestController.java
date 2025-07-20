package com.pitchain.dev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Value("#{'${spring.cors.allowed-origins}'.replaceAll(' ', '').split(',')}")
    private List<String> allowedOrigins;

    @GetMapping("/dev/allowedOrigins")
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }
}
