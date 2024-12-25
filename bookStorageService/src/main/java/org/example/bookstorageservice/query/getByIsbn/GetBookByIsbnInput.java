package org.example.bookstorageservice.query.getByIsbn;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookByIsbnInput implements Command<GetBookByIsbnOutput> {
    private String isbn;
}
