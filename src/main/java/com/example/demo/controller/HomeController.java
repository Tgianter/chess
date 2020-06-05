package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/home")
public class HomeController {
    @RequestMapping("/sendModel")
    public ModelAndView homeModel(){
        return new ModelAndView("homeModel");
    }
}
