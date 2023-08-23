package com.springtutorial.utils;


import com.springtutorial.models.Person;
import com.springtutorial.services.PeopleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PeopleValidator implements Validator {

    private final PeopleService peopleService;

    public PeopleValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Person person = (Person) o;

        if (peopleService.findByName(person.getName()).isPresent()){
            errors.rejectValue("name", "", "Such a person already exists!");
        }

    }
}
