package org.example.bookstorageservice.query.getById;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Data
@Component
public class GetBookByIdHandler implements Command.Handler<GetBookByIdInput, GetBookByIdOutput> {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public GetBookByIdOutput handle(GetBookByIdInput query) {
        return GetBookByIdOutput.output(Objects.requireNonNull(
                (bookRepository.findById(query.getId())).orElse(null)));
    }
}
