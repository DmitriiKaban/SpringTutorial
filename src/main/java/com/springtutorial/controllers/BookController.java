package com.springtutorial.controllers;

//import com.springtutorial.dao.PersonDAO;

import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import com.springtutorial.services.BookService;
import com.springtutorial.services.PeopleService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int currentPage,
                        @RequestParam(name = "size", defaultValue = "20") int pageSize,
                        @RequestParam(name = "sort_by_year", defaultValue = "false") boolean sortByYear) {

        Page<Book> bookPage;

        if (sortByYear) {
            bookPage = bookService.findPaginatedAndSortedByYear(PageRequest.of(currentPage - 1, pageSize));
        } else {
            bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("newBook") Book book) {

        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("newBook") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.show(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        bookService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.show(id));

        Person reader = bookService.getReader(id);
        System.out.println("Reader is " + reader);

        if (reader != null) {
            model.addAttribute("reader", reader);
        } else {
            model.addAttribute("people", peopleService.findAll());
            model.addAttribute("person", new Person());
        }

        return "books/show";
    }

    @PostMapping("/{id}/assign")
    public String addBookToPerson(@PathVariable("id") int bookId, @ModelAttribute("person") Person person) {
        bookService.assignBook(person.getId(), bookId);

        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);

        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);

        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook(@ModelAttribute("book") Book book) {

        return "books/search";
    }

    @PostMapping("/search")
    public String searchBookPost(@ModelAttribute("book") Book book, Model model) {

        System.out.println("Book to search: " + book.getName());


        return "redirect:/books/search/" + book.getName();
    }

    @GetMapping("/search/{name}")
    public String foundBooks(@ModelAttribute("book") Book book, @PathVariable("name") String bookName, Model model) {

        List<Book> books = bookService.findByNameStartingWith(bookName);
        if (books.isEmpty()) {
            model.addAttribute("no_books_found", true);
        }
        model.addAttribute("books", books);

        return "books/search";
    }
}
