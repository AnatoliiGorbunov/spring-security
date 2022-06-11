package ru.spring.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spring.security.entities.User;
import ru.spring.security.services.UserService;


import java.security.Principal;

@RestController
public class MainController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal) { //Principal - сжатая информация о пользователе// в любом эндпоинте где хотим получить информацию о пользователе
        //    Authentication a = SecurityContextHolder.getContext().getAuthentication();//альтернативный вариант Principal(то же самое)
        User user = userService.findByUsername(principal.getName());
//        return "secured part of web service: " + principal.getName();// получим имя пользователя
        return "secured part of web service " + user.getUsername() + " " + user.getEmail();
    }

    @GetMapping("/read_profile")
    public String pageForReadProfile(){
        return "read profile page";
    }

    @GetMapping("/only_for_admins")
    public String pageOnlyForAdmins(){
        return "admins page";
    }
}
