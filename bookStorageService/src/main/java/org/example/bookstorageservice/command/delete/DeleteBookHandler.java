package org.example.bookstorageservice.command.delete;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class DeleteBookHandler implements Command.Handler<DeleteBookInput, DeleteBookOutput> {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public DeleteBookOutput handle(DeleteBookInput command) {
        bookRepository.deleteById(command.getId());
        return new DeleteBookOutput();
    }
}
