package ru.kovalenkojuls.booksauthors.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateDTO {
    private String title;
    private String author;
    private int year;
}
