package org.example.booktrackerservice.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.booktrackerservice.model.BookStatus;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBookStatusesOutput {
    private Integer id;
    private Integer bookId;
    private Boolean isTaken;
    private Boolean isDeleted;
    private LocalTime taken;
    private LocalTime returned;
    private LocalTime deleted;

    public static List<GetAllBookStatusesOutput> output(List<BookStatus> bookStatuses) {
        List<GetAllBookStatusesOutput> bookStatusesOutput = new ArrayList<>();
        for (BookStatus bookStatus : bookStatuses) {
            GetAllBookStatusesOutput getAllBookStatusesOutput = new GetAllBookStatusesOutput();
            getAllBookStatusesOutput.setId(bookStatus.getId());
            getAllBookStatusesOutput.setBookId(bookStatus.getBookId());
            getAllBookStatusesOutput.setIsTaken(bookStatus.getIsTaken());
            getAllBookStatusesOutput.setIsDeleted(bookStatus.getIsDeleted());
            getAllBookStatusesOutput.setTaken(bookStatus.getTaken());
            getAllBookStatusesOutput.setReturned(bookStatus.getReturned());
            getAllBookStatusesOutput.setDeleted(bookStatus.getDeleted());
            bookStatusesOutput.add(getAllBookStatusesOutput);
        }
        return bookStatusesOutput;
    }
}
