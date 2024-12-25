package org.example.bookstorageservice.query.getById;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookByIdInput implements Command<GetBookByIdOutput> {
    private Integer id;
}
