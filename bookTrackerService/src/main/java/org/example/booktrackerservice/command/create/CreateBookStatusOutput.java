package org.example.booktrackerservice.command.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.booktrackerservice.model.BookStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookStatusOutput {
    private Integer id;
    private Integer bookId;
    private Boolean isTaken;
    private Boolean isDeleted;
    private LocalTime taken;
    private LocalTime returned;
    private LocalTime deleted;

    public static CreateBookStatusOutput output(BookStatus bookStatus) {
        return new CreateBookStatusOutput(
                bookStatus.getId(),
                bookStatus.getBookId(),
                bookStatus.getIsTaken(),
                bookStatus.getIsDeleted(),
                bookStatus.getTaken(),
                bookStatus.getReturned(),
                bookStatus.getDeleted());
    }
}
