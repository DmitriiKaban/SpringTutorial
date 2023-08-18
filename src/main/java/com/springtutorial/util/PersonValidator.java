package com.springtutorial.util;

import com.springtutorial.dao.PersonDAO;
import com.springtutorial.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass); // supports only Person objects
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        // check if a person with such an email already exists
        if (personDAO.show(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken!");
        }
    }
}
