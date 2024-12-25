package org.example.bookstorageservice.command.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstorageservice.model.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookOutput {
    private Integer id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

    public static CreateBookOutput output(Book book) {
        return new CreateBookOutput(book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getGenre(),
                book.getDescription(),
                book.getAuthor());
    }
}