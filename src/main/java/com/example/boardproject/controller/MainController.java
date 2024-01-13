package com.example.boardproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "forward:/articles";
    }
}
