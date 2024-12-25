package org.example.bookstorageservice.query.getAll;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class GetAllBooksHandler implements Command.Handler<GetAllBooksInput, List<GetAllBooksOutput>> {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<GetAllBooksOutput> handle(GetAllBooksInput query) {
        List<Book> books = bookRepository.findAll();
        return GetAllBooksOutput.output(books);
    }
}
