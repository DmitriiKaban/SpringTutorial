package com.springtutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {

    @GetMapping("hello")
    public String helloPage(@RequestParam("name") String name,
                            @RequestParam("surname") String surname){
        // reads /hello?name=Tom&surname=Jones

        System.out.println(name + " " + surname);

        return "first/hello";
    }
    @GetMapping("goodbye")
    public String goodbyePage(){
        return "first/goodbye";
    }
}
