package com.example.demo.services;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.BookRequest;
import com.example.demo.project.model.Author;
import com.example.demo.project.model.book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.bookrepository;

@Service
public class bookServices {

    private final bookrepository repository;
    private final AuthorRepository authorRepository;

    public bookServices(bookrepository repository, AuthorRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    public book addBook(BookRequest request) {
        Author author = authorRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Author not found"));

        if (request.getAuthorName() != null && !request.getAuthorName().isBlank()) {
            author = authorRepository.findAll().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(request.getAuthorName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Author not found with name: " + request.getAuthorName()));
        }

        book newBook = new book();
        newBook.setTitle(request.getTitle());
        newBook.setPrice(request.getPrice());
        newBook.setAuthor(author);
        return repository.save(newBook);
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