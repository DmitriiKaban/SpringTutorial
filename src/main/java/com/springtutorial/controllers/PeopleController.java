package com.springtutorial.controllers;

//import com.springtutorial.dao.PersonDAO;
import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import com.springtutorial.services.BookService;
import com.springtutorial.services.PeopleService;
import com.springtutorial.utils.PeopleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

//    private final PersonDAO personDAO;
    private final PeopleValidator peopleValidator;
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public PeopleController(PeopleValidator peopleValidator, PeopleService peopleService, BookService bookService) {
        this.peopleValidator = peopleValidator;
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model) {
        // receive people from DAO and send them to the view
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){

        // show a person by id
        model.addAttribute("person", peopleService.show(id));

        List<Book> books = bookService.getBooksByOwnerId(id);

        LocalDate currentDate = LocalDate.now();

        for (Book b: books) {
            if (b.getDateTaken() != null) {

                LocalDate localDateTaken = b.getDateTaken().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                long daysPassed = ChronoUnit.DAYS.between(localDateTaken, currentDate);

                b.setOutdated(daysPassed >= 10);
            }
        }

        if(!books.isEmpty()) {
            model.addAttribute("books", books);
        }

        return "people/show";
    }

    @GetMapping("/new")
    // Model Attribute will create a new object Person and will add to attributes to the model, so it can be accepted by our view
    public String newPerson(@ModelAttribute("person") Person person) {

        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        peopleValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new"; // return

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {

        model.addAttribute("person", peopleService.show(id));

        return "people/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        peopleValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }
}