package org.example.booktrackerservice.command.update.take;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeBookStatusInput implements Command<TakeBookStatusOutput> {
    private Integer bookId;
}
