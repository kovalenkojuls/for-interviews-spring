package ru.kovalenkojuls.booksauthors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kovalenkojuls.booksauthors.config.TestConfig;
import ru.kovalenkojuls.booksauthors.domain.Author;
import ru.kovalenkojuls.booksauthors.domain.Book;
import ru.kovalenkojuls.booksauthors.dto.BookCreateDTO;
import ru.kovalenkojuls.booksauthors.service.BookService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(TestConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("The green mile");
        book.setYear(1996);
        book.setAuthor(new Author(1L, "Steven King", new ArrayList<>()));

        Page<Book> page = new PageImpl<>(Collections.singletonList(book));

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {"content":[{"id":1,"title":"The green mile","year":1996,"author":{"id":1,"name":"Steven King"}}],"pageable":"INSTANCE","last":true,"totalPages":1,"totalElements":1,"first":true,"size":1,"number":0,"sort":{"empty":true,"sorted":false,"unsorted":true},"numberOfElements":1,"empty":false}
                """));
    }

    @Test
    public void testGetBookById_Success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("The Green Mile");
        book.setYear(1996);
        book.setAuthor(new Author(1L, "Steven King", new ArrayList<>()));

        when(bookService.getBookById(anyLong())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/{id}", 1L)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("The Green Mile"))
                .andExpect(jsonPath("$.year").value(1996));
    }

    @Test
    public void testGetBookById_NotFound() throws Exception {
        when(bookService.getBookById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", 1L)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBook() throws Exception {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setTitle("The Green Mile");
        bookCreateDTO.setYear(1996);
        bookCreateDTO.setAuthor("Steven King");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("The Green Mile");
        savedBook.setYear(1996);
        savedBook.setAuthor(new Author(1L, "Steven King", new ArrayList<>()));

        when(bookService.saveBook(any(BookCreateDTO.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("The Green Mile"))
                .andExpect(jsonPath("$.year").value(1996))
                .andExpect(jsonPath("$.author.name").value("Steven King"));
    }

    @Test
    public void testUpdateBook_Success() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("The Green Mile");
        updatedBook.setYear(1996);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book updated successfully");
        response.put("book", updatedBook);

        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(Optional.of(updatedBook));

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                .andExpect(jsonPath("$.book.id").value(1L))
                .andExpect(jsonPath("$.book.title").value("The Green Mile"))
                .andExpect(jsonPath("$.book.year").value(1996));
    }

    @Test
    public void testUpdateBook_NotFound() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("The Green Mile");
        updatedBook.setYear(1996);

        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBook_Success() throws Exception {
        when(bookService.deleteBook(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/books/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book deleted successfully"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteBook_NotFound() throws Exception {
        when(bookService.deleteBook(anyLong())).thenReturn(false);
        mockMvc.perform(delete("/api/books/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}