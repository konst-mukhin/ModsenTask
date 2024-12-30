package org.example.booktrackerservice.command.delete;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;

@Data
@Component
public class DeleteBookStatusHandler implements Command.Handler<DeleteBookStatusInput, DeleteBookStatusOutput>{
    @Autowired
    private BookStatusRepository bookStatusRepository;

    private final String kafkaTopic = "delete-topic";

    private final String kafkaGroupId = "group-id";

    @KafkaListener(topics = kafkaTopic, groupId = kafkaGroupId)
    public DeleteBookStatusOutput createStatus(Message myMsg) {
        Optional<BookStatus> bookStatus = bookStatusRepository.findByBookId(myMsg.getId());
        if(bookStatus.isPresent()) {
            bookStatus.get().setIsDeleted(true);
            bookStatus.get().setDeleted(LocalTime.now());
            bookStatusRepository.save(bookStatus.get());
        }
        return new DeleteBookStatusOutput();
    }

    @Override
    public DeleteBookStatusOutput handle(final DeleteBookStatusInput command) {
        throw new UnsupportedOperationException("Direct command handling is not implemented.");
    }
}
