package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.model.Author;
import com.example.demo.services.AuthorService;

@RestController
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @PostMapping("/authors")
    public Author addAuthor(@RequestBody Author author) {
        return service.addAuthor(author);
    }

    @GetMapping("/authors")
    public List<Author> getAuthors() {
        return service.getAuthors();
    }

    @GetMapping("/authors/{id}")
    public Author getAuthor(@PathVariable Long id) {
        return service.getAuthor(id);
    }

    @PutMapping("/authors/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        return service.updateAuthor(id, updatedAuthor);
    }

    @DeleteMapping("/authors/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        service.deleteAuthor(id);
        return "Author deleted successfully";
    }
}
