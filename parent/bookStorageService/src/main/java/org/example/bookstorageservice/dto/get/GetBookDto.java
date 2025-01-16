package org.example.bookstorageservice.dto.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookDto {
    private Integer id;

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
