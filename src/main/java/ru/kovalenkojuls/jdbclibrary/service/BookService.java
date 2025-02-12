package ru.kovalenkojuls.jdbclibrary.service;

import org.springframework.stereotype.Service;
import ru.kovalenkojuls.jdbclibrary.domain.Book;
import ru.kovalenkojuls.jdbclibrary.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }


    public Book updateBook(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book with ID " + book.getId() + " not found");
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
