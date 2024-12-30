package org.example.bookstorageservice.command;

import org.example.bookstorageservice.command.create.CreateBookHandler;
import org.example.bookstorageservice.command.create.CreateBookInput;
import org.example.bookstorageservice.command.create.CreateBookOutput;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateBookHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, Message> kafkaTemplate;

    @InjectMocks
    private CreateBookHandler createBookHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() {
        CreateBookInput input = new CreateBookInput();
        input.setIsbn("1234567890");
        input.setTitle("Test Book");
        input.setGenre("Fiction");
        input.setDescription("A test book description.");
        input.setAuthor("Test Author");

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setIsbn("1234567890");
        savedBook.setTitle("Test Book");
        savedBook.setGenre("Fiction");
        savedBook.setDescription("A test book description.");
        savedBook.setAuthor("Test Author");

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        CreateBookOutput output = createBookHandler.handle(input);

        assertNotNull(output);
        assertEquals(1, output.getId());
        assertEquals("Test Book", output.getTitle());
        assertEquals("1234567890", output.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(kafkaTemplate, times(1)).send(eq("create-topic"), any(Message.class));
    }
}
