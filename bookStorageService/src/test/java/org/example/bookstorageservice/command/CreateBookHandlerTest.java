package org.example.bookstorageservice.command;

import org.example.bookstorageservice.command.create.CreateBookHandler;
import org.example.bookstorageservice.command.create.CreateBookInput;
import org.example.bookstorageservice.command.create.CreateBookOutput;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class CreateBookHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CreateBookHandler createBookHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldCreateBookAndReturnOutput() {
        CreateBookInput input = new CreateBookInput();
        input.setIsbn("1234567890");
        input.setTitle("Test Book");
        input.setGenre("Fiction");
        input.setDescription("Test Description");
        input.setAuthor("Test Author");

        Book savedBook = new Book();
        savedBook.setIsbn("1234567890");
        savedBook.setTitle("Test Book");
        savedBook.setGenre("Fiction");
        savedBook.setDescription("Test Description");
        savedBook.setAuthor("Test Author");

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        CreateBookOutput output = createBookHandler.handle(input);

        assertEquals("1234567890", output.getIsbn());
        assertEquals("Test Book", output.getTitle());
        assertEquals("Fiction", output.getGenre());
        assertEquals("Test Description", output.getDescription());
        assertEquals("Test Author", output.getAuthor());

        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
