package org.example.bookstorageservice.query.getAll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstorageservice.model.Book;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBooksOutput {
    private Integer id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

    public static List<GetAllBooksOutput> output(List<Book> books) {
        List<GetAllBooksOutput> booksOutput = new ArrayList<>();
        for (Book book : books) {
            GetAllBooksOutput getAllBooksOutput = new GetAllBooksOutput();
            getAllBooksOutput.setId(book.getId());
            getAllBooksOutput.setIsbn(book.getIsbn());
            getAllBooksOutput.setTitle(book.getTitle());
            getAllBooksOutput.setGenre(book.getGenre());
            getAllBooksOutput.setDescription(book.getDescription());
            getAllBooksOutput.setAuthor(book.getAuthor());
            booksOutput.add(getAllBooksOutput);
        }
        return booksOutput;
    }
}
