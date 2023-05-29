package ru.sonyabeldy.springcourse.FirstRestApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sonyabeldy.springcourse.FirstRestApp.models.Person;
import ru.sonyabeldy.springcourse.FirstRestApp.services.PeopleService;
import ru.sonyabeldy.springcourse.FirstRestApp.utils.PersonErrorResponse;
import ru.sonyabeldy.springcourse.FirstRestApp.utils.PersonNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    public final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public List<Person> getPeople() {
        return peopleService.findAll(); //Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        return peopleService.findOne(id); //jackson автоматически конвертирует в JSON
    }


    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );

        // в HTT
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //not_fount - 404 статус
    }
}
