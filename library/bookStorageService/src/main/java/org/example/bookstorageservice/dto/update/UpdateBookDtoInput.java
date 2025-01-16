package org.example.bookstorageservice.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDtoInput {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
