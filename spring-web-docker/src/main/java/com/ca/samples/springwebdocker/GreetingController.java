package com.ca.samples.springwebdocker;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@Controller
public class GreetingController {
    @GetMapping("/greeting")
    public String greet(@RequestParam (name = "name", required = false, defaultValue = "Spring Booter") String  name, Model model) {
        model.addAttribute("name", name);
        return "greeting"; 
    }
}
