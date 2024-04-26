package com.example.project_3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping({  "", "/"})
    public String index() {
        return "login/index";
    }

}
