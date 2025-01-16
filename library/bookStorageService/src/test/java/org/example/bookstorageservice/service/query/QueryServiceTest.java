package org.example.bookstorageservice.service.query;

import org.example.bookstorageservice.dto.get.GetBookDto;
import org.example.bookstorageservice.exception.NotFound;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryServiceTest {

    private QueryService queryService;

    @Mock
    private BookRepository bookRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        queryService = new QueryService(modelMapper, bookRepository);
    }

    @Test
    void getAll_Success() throws NotFound {
        Book book1 = new Book(1, "1234567890123", "Title1", "Fiction", "Description1", "Author1");
        Book book2 = new Book(2, "9876543210987", "Title2", "Non-Fiction", "Description2", "Author2");
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<GetBookDto> result = queryService.getAll();

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Fiction", result.get(0).getGenre());
        assertEquals("Description1", result.get(0).getDescription());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAll_NotFound() {
        when(bookRepository.findAll()).thenReturn(List.of());

        NotFound exception = assertThrows(NotFound.class, queryService::getAll);
        assertEquals("List is empty", exception.getMessage());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getById_Success() throws NotFound {
        Book book = new Book(1, "1234567890123", "Title1", "Fiction", "Description1", "Author1");
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        GetBookDto result = queryService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("1234567890123", result.getIsbn());
        assertEquals("Title1", result.getTitle());
        assertEquals("Fiction", result.getGenre());
        assertEquals("Description1", result.getDescription());
        assertEquals("Author1", result.getAuthor());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test
    void getById_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> queryService.getById(1));
        assertEquals("Wrong id", exception.getMessage());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test
    void getByIsbn_Success() throws NotFound {
        Book book = new Book(1, "1234567890123", "Title1", "Fiction", "Description1", "Author1");
        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(book));

        GetBookDto result = queryService.getByIsbn("1234567890123");

        assertEquals(1, result.getId());
        assertEquals("1234567890123", result.getIsbn());
        assertEquals("Title1", result.getTitle());
        assertEquals("Fiction", result.getGenre());
        assertEquals("Description1", result.getDescription());
        assertEquals("Author1", result.getAuthor());
        verify(bookRepository, times(1)).findByIsbn("1234567890123");
    }

    @Test
    void getByIsbn_NotFound() {
        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> queryService.getByIsbn("1234567890123"));
        assertEquals("Wrong isbn", exception.getMessage());
        verify(bookRepository, times(1)).findByIsbn("1234567890123");
    }
}
