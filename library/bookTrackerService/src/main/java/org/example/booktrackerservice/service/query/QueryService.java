package org.example.booktrackerservice.service.query;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.booktrackerservice.dto.get.GetBookStatusDto;
import org.example.booktrackerservice.exception.NotFound;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class QueryService {
    private final ModelMapper modelMapper;
    private BookStatusRepository bookStatusRepository;

    public List<GetBookStatusDto> getAll() throws NotFound {
        List<BookStatus> bookStatuses = bookStatusRepository.findAll();
        if (bookStatuses.isEmpty()) {
            throw new NotFound("List is empty");
        }
        List<GetBookStatusDto> bookStatusDtos = new ArrayList<>();
        for (BookStatus bookStatus : bookStatuses) {
            if(!bookStatus.getIsTaken()){
                bookStatusDtos.add(modelMapper.map(bookStatus, GetBookStatusDto.class));
            }
        }
        return bookStatusDtos;
    }
}
