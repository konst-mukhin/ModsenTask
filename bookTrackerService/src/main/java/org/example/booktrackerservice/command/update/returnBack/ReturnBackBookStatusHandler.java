package org.example.booktrackerservice.command.update.returnBack;

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
public class ReturnBackBookStatusHandler implements Command.Handler<ReturnBackBookStatusInput, ReturnBackBookStatusOutput> {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Override
    public ReturnBackBookStatusOutput handle(ReturnBackBookStatusInput command) {
        Optional<BookStatus> bookStatus = bookStatusRepository.findByBookId(command.getBookId());
        if(bookStatus.isEmpty() || bookStatus.get().getIsDeleted()) {
            assert bookStatus.orElse(null) != null;
            return ReturnBackBookStatusOutput.output(bookStatus.orElse(null));
        }
        if(bookStatus.get().getIsTaken()) {
            bookStatus.get().setIsTaken(false);
            bookStatus.get().setReturned(LocalTime.now());
            bookStatusRepository.save(bookStatus.get());
        }
        return ReturnBackBookStatusOutput.output(bookStatus.orElse(null));
    }
}
