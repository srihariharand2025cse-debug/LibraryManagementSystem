package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.model.book;
import com.example.demo.services.bookServices;

@RestController
public class bookcontroller {

    private final bookServices service;

    public bookcontroller(bookServices service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "Book API is running. Use /books for CRUD operations.";
    }

    @PostMapping("/books")
    public book addBook(@RequestBody book b) {
        return service.addBook(b);
    }

    @GetMapping("/books")
    public List<book> getBooks() {
        return service.getBooks();
    }

    @GetMapping("/books/{id}")
    public book getBook(@PathVariable Long id) {
        return service.getBook(id);
    }

    @PutMapping("/books/{id}")
    public book updateBook(@PathVariable Long id, @RequestBody book updatedBook) {
        return service.updateBook(id, updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id) {
        service.deleteBook(id);
        return "Book deleted successfully";
    }
}