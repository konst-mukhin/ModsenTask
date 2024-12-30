package org.example.booktrackerservice.command.update.take;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;

@Data
@Component
public class TakeBookStatusHandler implements Command.Handler<TakeBookStatusInput, TakeBookStatusOutput> {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Override
    public TakeBookStatusOutput handle(TakeBookStatusInput command) {
        Optional<BookStatus> bookStatus = bookStatusRepository.findByBookId(command.getBookId());
        if(bookStatus.isEmpty() || bookStatus.get().getIsDeleted()) {
            assert bookStatus.orElse(null) != null;
            return TakeBookStatusOutput.output(bookStatus.orElse(null));
        }
        if(!(bookStatus.get().getIsTaken())){
            bookStatus.get().setIsTaken(true);
            bookStatus.get().setTaken(LocalTime.now());
            bookStatus.get().setReturned(null);
            bookStatusRepository.save(bookStatus.get());
        }
        return TakeBookStatusOutput.output(bookStatus.orElse(null));
    }
}
