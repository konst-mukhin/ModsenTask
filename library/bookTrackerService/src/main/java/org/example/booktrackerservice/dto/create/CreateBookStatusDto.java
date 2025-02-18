package org.example.booktrackerservice.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookStatusDto {
    private Integer id;

    private Integer bookId;

    private Boolean isTaken;

    private Boolean isDeleted;

    private LocalTime taken;

    private LocalTime returned;

    private LocalTime deleted;
}
