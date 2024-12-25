package org.example.bookstorageservice.command.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstorageservice.model.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookOutput {
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

    public static UpdateBookOutput output(Book book) {
        return new UpdateBookOutput(
                book.getIsbn(),
                book.getTitle(),
                book.getGenre(),
                book.getDescription(),
                book.getAuthor());
    }
}
