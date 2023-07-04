package com.project.security.controller.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello world");
    }

}
