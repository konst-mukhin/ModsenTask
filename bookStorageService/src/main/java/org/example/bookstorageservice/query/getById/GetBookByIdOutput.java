package org.example.bookstorageservice.query.getById;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstorageservice.model.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookByIdOutput {
    private Integer id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

    public static GetBookByIdOutput output(Book book) {
        return new GetBookByIdOutput(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getGenre(),
                book.getDescription(),
                book.getAuthor()
        );
    }
}
