package org.example.bookstorageservice.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDtoInput {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
