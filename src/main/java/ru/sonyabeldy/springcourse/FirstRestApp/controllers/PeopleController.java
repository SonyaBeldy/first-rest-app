package ru.sonyabeldy.springcourse.FirstRestApp.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sonyabeldy.springcourse.FirstRestApp.dto.PersonDTO;
import ru.sonyabeldy.springcourse.FirstRestApp.models.Person;
import ru.sonyabeldy.springcourse.FirstRestApp.services.PeopleService;
import ru.sonyabeldy.springcourse.FirstRestApp.utils.PersonErrorResponse;
import ru.sonyabeldy.springcourse.FirstRestApp.utils.PersonNotCreatedException;
import ru.sonyabeldy.springcourse.FirstRestApp.utils.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMsg.toString());
        }

        peopleService.save(convertToPerson(personDTO));

        // отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
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

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        // в HTT
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //not_fount - 404 статус
    }

    private Person convertToPerson(PersonDTO personDTO) {
        Person person = new Person();

        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        person.setEmail(personDTO.getEmail());

        return person;
    }

}
