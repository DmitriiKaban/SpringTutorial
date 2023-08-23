package com.springtutorial.services;

import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import com.springtutorial.repositories.BookRepository;
import com.springtutorial.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    public final BookRepository bookRepository;
    public final PeopleRepository personRepository;

    public BookService(BookRepository bookRepository, PeopleRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> index() {
        return bookRepository.findAll();
    }

    public List<Book> index(int pageNumber, int itemsPerPage) {
        return bookRepository.findAll(PageRequest.of(pageNumber, itemsPerPage)).getContent();
    }


    public void save(Book book) {
        bookRepository.save(book);
    }


    public Book show(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public Person getReader(int id) {
        Optional<Book> book = bookRepository.findById(id);

        return book.map(Book::getOwner).orElse(null);
    }

    public void assignBook(int id, int bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Optional<Person> person = personRepository.findById(id);

            if (person.isPresent()) {
                Book bookToUpdate = book.get();
                bookToUpdate.setOwner(person.get());
                bookToUpdate.setDateTaken(new Date());
                bookRepository.save(bookToUpdate);
            }
        }
    }

    public void release(int id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            Book b = book.get();
            b.setOwner(null);
            b.setDateTaken(null);
            bookRepository.save(b);
        }
    }

    public List<Book> getBooksByOwnerId(int id) {

        Person owner = personRepository.findById(id).orElse(null);

        if (owner != null) {
            Hibernate.initialize(owner.getBooks());
            return owner.getBooks();
        }
        return null;
    }

    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByNameStartingWith(String name) {
        return bookRepository.findByNameStartingWith(name);
    }

    public Page<Book> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;

        List<Book> books = bookRepository.findAll();

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        Page<Book> bookPage
                = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }

    public Page<Book> findPaginatedAndSortedByYear(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;

        Sort sortByYear = Sort.by("year").ascending(); // Create a sorting object for year field

        List<Book> books = bookRepository.findAll(sortByYear); // Fetch books sorted by year

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), books.size());
    }
}
