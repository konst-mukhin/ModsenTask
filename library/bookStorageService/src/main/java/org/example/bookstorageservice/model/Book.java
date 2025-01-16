package org.example.bookstorageservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_book")
public class Book {
    @Id
    @GeneratedValue
    private Integer id;

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
