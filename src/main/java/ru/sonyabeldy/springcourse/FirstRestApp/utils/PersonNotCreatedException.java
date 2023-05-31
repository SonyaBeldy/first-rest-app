package ru.sonyabeldy.springcourse.FirstRestApp.utils;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String msg) {
        super(msg);
    }
}
