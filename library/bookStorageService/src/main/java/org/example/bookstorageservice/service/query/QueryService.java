package org.example.bookstorageservice.service.query;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.bookstorageservice.dto.get.GetBookDto;
import org.example.bookstorageservice.exception.EntityNotFound;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class QueryService {
    private final ModelMapper modelMapper;
    private BookRepository bookRepository;

    public List<GetBookDto> getAll() throws EntityNotFound {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFound("List is empty");
        }
        return Arrays.asList(modelMapper.map(books, GetBookDto[].class));
    }

    public GetBookDto getById(Integer id) throws EntityNotFound {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFound("Wrong id"));
        return modelMapper.map(book, GetBookDto.class);
    }

    public GetBookDto getByIsbn(String isbn) throws EntityNotFound {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFound("Wrong isbn"));
        return modelMapper.map(book, GetBookDto.class);
    }
}
