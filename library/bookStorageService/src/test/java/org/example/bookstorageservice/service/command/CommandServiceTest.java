package org.example.bookstorageservice.service.command;

import org.example.bookstorageservice.dto.create.CreateBookDtoInput;
import org.example.bookstorageservice.dto.create.CreateBookDtoOutput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoInput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoOutput;
import org.example.bookstorageservice.exception.IsbnAlreadyExists;
import org.example.bookstorageservice.exception.EntityNotFound;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, Message> kafkaTemplate;

    @InjectMocks
    private CommandService commandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_Success() throws IsbnAlreadyExists {
        CreateBookDtoInput input = new CreateBookDtoInput("1234567890", "Title", "Author", "Genre", "Description");
        Book book = new Book(1, "1234567890", "Title", "Author", "Genre", "Description");

        when(bookRepository.findByIsbn(input.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(input, Book.class)).thenReturn(book);
        when(modelMapper.map(book, CreateBookDtoOutput.class)).thenReturn(new CreateBookDtoOutput(1, "1234567890", "Title", "Author", "Genre", "Description"));

        CreateBookDtoOutput result = commandService.create(input);

        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        verify(bookRepository, times(1)).save(book);
        verify(kafkaTemplate, times(1)).send(eq("create-topic"), any(Message.class));
    }

    @Test
    void create_BadRequest() {
        CreateBookDtoInput input = new CreateBookDtoInput("1234567890", "Title", "Author", "Genre", "Description");
        when(bookRepository.findByIsbn(input.getIsbn())).thenReturn(Optional.of(new Book()));

        IsbnAlreadyExists exception = assertThrows(IsbnAlreadyExists.class, () -> commandService.create(input));
        assertEquals("Already exists. Change isbn", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void update_Success() throws EntityNotFound {
        Integer bookId = 1;
        UpdateBookDtoInput input = new UpdateBookDtoInput("1234567890", "New Title", "New Author", "New Genre", "New Description");
        Book existingBook = new Book(bookId, "1234567890", "Title", "Author", "Genre", "Description");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(existingBook));
        when(modelMapper.map(input, Book.class)).thenReturn(existingBook);
        when(modelMapper.map(existingBook, UpdateBookDtoOutput.class)).thenReturn(new UpdateBookDtoOutput("1234567890", "New Title", "New Author", "New Genre", "New Description"));

        UpdateBookDtoOutput result = commandService.update(bookId, input);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void update_NotFound() {
        Integer bookId = 1;
        UpdateBookDtoInput input = new UpdateBookDtoInput("1234567890", "New Title", "New Author", "New Genre", "New Description");
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class, () -> commandService.update(bookId, input));
        assertEquals("Empty", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void delete_Success() throws EntityNotFound {
        Integer bookId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        commandService.delete(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
        verify(kafkaTemplate, times(1)).send(eq("delete-topic"), any(Message.class));
    }

    @Test
    void delete_NotFound() {
        Integer bookId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class, () -> commandService.delete(bookId));
        assertEquals("Wrong id", exception.getMessage());
        verify(bookRepository, never()).deleteById(bookId);
    }
}
