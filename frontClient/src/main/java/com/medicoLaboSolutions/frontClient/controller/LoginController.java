package com.medicoLaboSolutions.frontClient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class serves as a main controller of the whole application.
 * It serves as an authentication request controller
 *
 * For each request of the application, the user must be authenticated.
 * There he will be redirected to the login page before anything else.
 *
 * @version 1.0
 */
@Controller
public class LoginController {
    @GetMapping("/login")
    String login() {
        return "login";
    }
}
