package com.glrtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glrtech.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
