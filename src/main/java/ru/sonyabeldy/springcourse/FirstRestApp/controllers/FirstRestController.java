package ru.sonyabeldy.springcourse.FirstRestApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class FirstRestController {

    @ResponseBody // главная не возвращаем название представления, а данные
    @GetMapping("/sayHello")
    public String sayHello() { //будет сконверитирован джексоном в строку
        return "Hello World!";
    }


}
