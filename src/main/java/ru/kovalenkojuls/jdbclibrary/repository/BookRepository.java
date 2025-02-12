package ru.kovalenkojuls.jdbclibrary.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kovalenkojuls.jdbclibrary.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?) RETURNING id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM books WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM books";
    private static final String SQL_UPDATE = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM books WHERE id = ?";

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = new RowMapper<>() {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));

            return book;
        }
    };

    public Book save(Book book) {
        if (book.getId() == null) {
            Long generatedId = jdbcTemplate.queryForObject(SQL_INSERT, Long.class, book.getTitle(), book.getAuthor(), book.getPublicationYear());
            book.setId(generatedId);
        } else {
            jdbcTemplate.update(SQL_UPDATE, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
        }
        return book;
    }

    public Optional<Book> findById(Long id) {
        List<Book> books = jdbcTemplate.query(SQL_SELECT_BY_ID, bookRowMapper, id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    public List<Book> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, bookRowMapper);
    }


    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    public boolean existsById(Long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM book WHERE id = ?)", Boolean.class, id)
        );
    }
}
