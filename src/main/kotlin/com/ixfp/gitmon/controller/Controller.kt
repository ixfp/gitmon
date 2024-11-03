package com.ixfp.gitmon.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class Controller {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, World!"
    }
}