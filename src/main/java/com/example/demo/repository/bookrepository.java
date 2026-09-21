package com.example.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.project.model.book;

public interface bookrepository extends JpaRepository<book, Long> {
}
