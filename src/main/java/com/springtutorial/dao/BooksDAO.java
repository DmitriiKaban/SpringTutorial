package com.springtutorial.dao;


import com.springtutorial.models.Book;
import com.springtutorial.models.Person;
import com.springtutorial.utils.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BooksDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BooksDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BookMapper()).stream().findAny().orElse(null);
    }

    public void save(Book book) {

        jdbcTemplate.update("INSERT INTO book(name, author, year, person_id) VALUES (?, ?, ?, null)", book.getName(), book.getAuthorName(), book.getYear());
    }

    public void update(int id, Book updatedBook) {

        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?", updatedBook.getName(), updatedBook.getAuthorName(), updatedBook.getYear(), id);
    }

    public void setReader(int id, int personId) {

        jdbcTemplate.update("update book set person_id=? where id=?", personId, id);
    }

    public Person getReader(int id) {

        Person p = jdbcTemplate.query("select p.name from person p JOIN book b ON p.id=b.person_id AND b.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);

        return p;
    }

    public void delete(int id) {

        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }


    public void giveBook(int personId, int bookId) {

        jdbcTemplate.update("UPDATE BOOK SET person_id=? WHERE id=?", personId, bookId);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE BOOK SET person_id=? WHERE id=?", null, id);
    }
}