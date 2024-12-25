package org.example.bookstorageservice.query.getByIsbn;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Data
@Component
public class GetBookByIsbnHandler implements Command.Handler<GetBookByIsbnInput, GetBookByIsbnOutput> {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public GetBookByIsbnOutput handle(GetBookByIsbnInput query) {
        return GetBookByIsbnOutput.output(Objects.requireNonNull(
                (bookRepository.findByIsbn(query.getIsbn())).orElse(null)));
    }
}
