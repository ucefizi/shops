package com.izi.shops.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestResource {

    @GetMapping("/test")
    public String test() {
        return "If you see this, it means you're authenticated.";
    }
}
