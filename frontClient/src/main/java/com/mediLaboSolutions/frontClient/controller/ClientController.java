package com.mediLaboSolutions.frontClient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping(path = "/")
    public String homePage(Model model){
        return "home";
    }
}
