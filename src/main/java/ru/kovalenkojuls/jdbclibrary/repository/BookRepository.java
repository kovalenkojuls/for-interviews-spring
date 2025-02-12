package ru.kovalenkojuls.jdbclibrary.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kovalenkojuls.jdbclibrary.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}
