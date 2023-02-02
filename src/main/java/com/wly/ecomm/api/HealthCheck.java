package com.wly.ecomm.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
For aws deployment heartbeat check
 */

@RestController
@RequestMapping("/")
public class HealthCheck {
    @GetMapping()
    public String iAmOkay() {
        return "OK";
    }
}
