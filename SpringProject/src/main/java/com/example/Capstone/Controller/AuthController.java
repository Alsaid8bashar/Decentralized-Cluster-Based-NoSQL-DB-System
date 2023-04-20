package com.example.Capstone.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.Authentication.UserAuthentication;
import org.example.Facade.DataBaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Auth")
public class AuthController {
    @Autowired
    private UserAuthentication  userAuthentication;

    public UserAuthentication userAuth() {
        return userAuthentication;
    }

    @PostMapping("/")
    public String login(String token, HttpSession session) {
        Integer id=userAuthentication.isUserAuthenticate(token);
        if (id>0) {
            session.setAttribute("userDb",new DataBaseFacade<>(id));
            return "welcome back!";
        } else return "you are not allowed to be here !";

    }
}
