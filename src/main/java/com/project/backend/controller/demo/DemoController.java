package com.project.backend.controller.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
@Tag(name = "Demo")
public class DemoController {

    @Operation(
            description = "Returns a hello message",
            summary = "Summary for hello operation"
    )
    @GetMapping
//    @Hidden
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello world");
    }

}
