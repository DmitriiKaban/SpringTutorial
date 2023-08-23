package com.springtutorial.repositories;

import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByNameStartingWith(String name);
}
