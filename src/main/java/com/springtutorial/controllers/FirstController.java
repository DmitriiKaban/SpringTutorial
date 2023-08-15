package com.springtutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {

    @GetMapping("hello")
    public String helloPage(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "surname", required = false) String surname){
        // reads /hello?name=Tom&surname=Jones

        System.out.println(name + " " + surname);

        return "first/hello";
    }
    @GetMapping("goodbye")
    public String goodbyePage(){
        return "first/goodbye";
    }
}
