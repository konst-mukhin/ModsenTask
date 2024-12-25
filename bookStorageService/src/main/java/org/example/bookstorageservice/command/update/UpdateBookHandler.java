package org.example.bookstorageservice.command.update;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Component
public class UpdateBookHandler implements Command.Handler<UpdateBookInput, UpdateBookOutput> {
    @Autowired
    private BookRepository bookRepository;


    @Override
    public UpdateBookOutput handle(UpdateBookInput command) {
        Optional<Book> book = bookRepository.findById(command.getId());
        book.ifPresent(b -> {
            b.setIsbn(command.getIsbn());
            b.setTitle(command.getTitle());
            b.setGenre(command.getGenre());
            b.setDescription(command.getDescription());
            b.setAuthor(command.getAuthor());
            bookRepository.save(book.get());
        });
        assert book.orElse(null) != null;
        return UpdateBookOutput.output(book.orElse(null));
    }
}
