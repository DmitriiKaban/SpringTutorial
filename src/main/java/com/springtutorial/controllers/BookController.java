package com.springtutorial.controllers;

import com.springtutorial.dao.BooksDAO;
import com.springtutorial.dao.PersonDAO;
import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksDAO booksDAO;
    private final PersonDAO personDAO;


    public BookController(BooksDAO booksDAO, PersonDAO personDAO) {
        this.booksDAO = booksDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", booksDAO.index());
        for(Book b: booksDAO.index())
            System.out.println(b);
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("newBook") Book book){

        System.out.println(book);
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("newBook") @Valid Book book, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return "books/new";

        booksDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksDAO.show(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id){
        booksDAO.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksDAO.show(id));

        Person reader = booksDAO.getReader(id);
        System.out.println(reader);

        if (reader != null) {
            model.addAttribute("reader", reader);
        } else {
            model.addAttribute("people", personDAO.index());
            model.addAttribute("person", new Person());
        }

        return "books/show";
    }

    @PostMapping("/{id}/assign")
    public String addBookToPerson(@PathVariable("id") int bookId, @ModelAttribute("person") Person person){
        booksDAO.giveBook(person.getId(), bookId);

        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        booksDAO.release(id);

        return "redirect:/books/" + id;
    }
}
