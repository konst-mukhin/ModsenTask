package org.example.booktrackerservice.command.delete;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBookStatusInput implements Command<DeleteBookStatusOutput> {
    private Integer bookId;
}
