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
}
