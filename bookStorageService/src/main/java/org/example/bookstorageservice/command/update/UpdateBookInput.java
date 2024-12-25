package org.example.bookstorageservice.command.update;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookInput implements Command<UpdateBookOutput> {
    private Integer id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}
