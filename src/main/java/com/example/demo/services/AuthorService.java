package com.example.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.project.model.Author;
import com.example.demo.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Author addAuthor(Author author) {
        return repository.save(author);
    }

    public List<Author> getAuthors() {
        return repository.findAll();
    }

    public Author getAuthor(Long id) {
        return findAuthor(id);
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        Author existingAuthor = findAuthor(id);
        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setCountry(updatedAuthor.getCountry());
        return repository.save(existingAuthor);
    }

    public void deleteAuthor(Long id) {
        repository.delete(findAuthor(id));
    }

    private Author findAuthor(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }
}
