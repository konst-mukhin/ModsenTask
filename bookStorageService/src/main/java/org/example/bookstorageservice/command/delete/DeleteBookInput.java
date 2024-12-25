package org.example.bookstorageservice.command.delete;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBookInput implements Command<DeleteBookOutput>{
    private Integer id;
}
