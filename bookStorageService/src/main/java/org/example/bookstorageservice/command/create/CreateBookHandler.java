package org.example.bookstorageservice.command.create;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateBookHandler implements Command.Handler<CreateBookInput, CreateBookOutput> {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    private static final String TOPIC_NAME = "create-topic";

    @Override
    public CreateBookOutput handle(final CreateBookInput command)  {
        Book book = new Book();
        book.setIsbn(command.getIsbn());
        book.setTitle(command.getTitle());
        book.setGenre(command.getGenre());
        book.setDescription(command.getDescription());
        book.setAuthor(command.getAuthor());
        Book createdBook = bookRepository.save(book);
        Message message = new Message(createdBook.getId());
        kafkaTemplate.send(TOPIC_NAME, message);
        return CreateBookOutput.output(createdBook);
    }
}
