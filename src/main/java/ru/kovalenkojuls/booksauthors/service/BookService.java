package ru.kovalenkojuls.booksauthors.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kovalenkojuls.booksauthors.domain.Author;
import ru.kovalenkojuls.booksauthors.domain.Book;
import ru.kovalenkojuls.booksauthors.dto.BookCreateDTO;
import ru.kovalenkojuls.booksauthors.repository.AuthorRepository;
import ru.kovalenkojuls.booksauthors.repository.BookRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book saveBook(BookCreateDTO bookRequest) {
        Author author = authorRepository.findByName(bookRequest.getAuthor())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(bookRequest.getAuthor());
                    return authorRepository.save(newAuthor);
                });

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setYear(bookRequest.getYear());
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setYear(updatedBook.getYear());
                    return bookRepository.save(book);
                });
    }

    public boolean deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return true;
                }).orElse(false);
    }
}