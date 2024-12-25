package org.example.bookstorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDto {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
