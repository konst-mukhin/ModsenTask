package org.example.booktrackerservice.query;

import an.awesome.pipelinr.Command;
import lombok.Data;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class GetAllBookStatusesHandler implements Command.Handler<GetAllBookStatusesInput, List<GetAllBookStatusesOutput>> {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Override
    public List<GetAllBookStatusesOutput> handle(GetAllBookStatusesInput query) {
        List<BookStatus> bookStatuses = bookStatusRepository.findAll();
        for (BookStatus bookStatus : bookStatuses) {
            if(bookStatus.getIsTaken()){
                bookStatuses.remove(bookStatus);
            }
        }
        return GetAllBookStatusesOutput.output(bookStatuses);
    }
}
