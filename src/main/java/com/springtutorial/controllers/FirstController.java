package com.springtutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {

    @GetMapping("hello")
    public String helloPage(HttpServletRequest request){
        // reads /hello?name=Tom&surname=Jones
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        System.out.println(name + " " + surname);

        return "first/hello";
    }
    @GetMapping("goodbye")
    public String goodbyePage(){
        return "first/goodbye";
    }
}
