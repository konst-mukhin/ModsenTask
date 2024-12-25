package org.example.bookstorageservice.command.create;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookInput implements Command<CreateBookOutput> {
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

//    public CreateBookInput(final String isbn,
//                           final String title,
//                           final String genre,
//                           final String description,
//                           final String author) {
//        this.isbn = isbn;
//        this.title = title;
//        this.genre = genre;
//        this.description = description;
//        this.author = author;
//    }
}
