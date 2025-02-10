package ru.kovalenkojuls.booksauthors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kovalenkojuls.booksauthors.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
