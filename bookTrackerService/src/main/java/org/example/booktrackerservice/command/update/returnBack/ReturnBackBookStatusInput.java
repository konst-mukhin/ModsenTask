package org.example.booktrackerservice.command.update.returnBack;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBackBookStatusInput implements Command<ReturnBackBookStatusOutput> {
    private Integer bookId;
}
