package com.example.demo.services;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.project.model.book;
import com.example.demo.repository.bookrepository;

@Service
public class bookServices {

    private final bookrepository repository;

    public bookServices(bookrepository repository) {
        this.repository = repository;
    }

    public book addBook(book book) {
        return repository.save(book);
    }

    public List<book> getBooks() {
        return repository.findAll();
    }

    public book getBook(Long id) {
        return findBook(id);
    }

    public book updateBook(Long id, book updatedBook) {
        book existingBook = findBook(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPrice(updatedBook.getPrice());
        return repository.save(existingBook);
    }

    public void deleteBook(Long id) {
        repository.delete(findBook(id));
    }

    private book findBook(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Book not found"));
    }
}