package org.example.bookstorageservice.command.delete;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Data
@Component
public class DeleteBookHandler implements Command.Handler<DeleteBookInput, DeleteBookOutput> {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    private static final String TOPIC_NAME = "delete-topic";

    @Override
    public DeleteBookOutput handle(DeleteBookInput command) {
        bookRepository.deleteById(command.getId());
        Message message = new Message(command.getId());
        kafkaTemplate.send(TOPIC_NAME, message);
        return new DeleteBookOutput();
    }
}
