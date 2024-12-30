package org.example.booktrackerservice.command.create;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateBookStatusHandler implements Command.Handler<CreateBookStatusInput, CreateBookStatusOutput> {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    private final String kafkaTopic = "create-topic";

    private final String kafkaGroupId = "group-id";

    @KafkaListener(topics = kafkaTopic, groupId = kafkaGroupId)
    public CreateBookStatusOutput createStatus(Message myMsg) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(myMsg.getId());
        bookStatus.setIsTaken(false);
        bookStatus.setIsDeleted(false);
        bookStatus.setTaken(null);
        bookStatus.setReturned(null);
        bookStatus.setDeleted(null);
        BookStatus createdBookStatus = bookStatusRepository.save(bookStatus);
        return CreateBookStatusOutput.output(createdBookStatus);
    }

    @Override
    public CreateBookStatusOutput handle(final CreateBookStatusInput command) {
        throw new UnsupportedOperationException("Direct command handling is not implemented.");
    }
}
