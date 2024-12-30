package org.example.booktrackerservice.command.update.returnBack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.booktrackerservice.model.BookStatus;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBackBookStatusOutput {
    private Integer bookId;
    private Boolean isTaken;
    private Boolean isDeleted;
    private LocalTime taken;
    private LocalTime returned;
    private LocalTime deleted;

    public static ReturnBackBookStatusOutput output(BookStatus bookStatus) {
        return new ReturnBackBookStatusOutput(
                bookStatus.getBookId(),
                bookStatus.getIsTaken(),
                bookStatus.getIsDeleted(),
                bookStatus.getTaken(),
                bookStatus.getReturned(),
                bookStatus.getDeleted());
    }
}
