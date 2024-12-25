package org.example.bookstorageservice.command;

import org.example.bookstorageservice.command.update.UpdateBookHandler;
import org.example.bookstorageservice.command.update.UpdateBookInput;
import org.example.bookstorageservice.command.update.UpdateBookOutput;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UpdateBookHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UpdateBookHandler updateBookHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldUpdateBookDetails() {
        Integer bookId = 1;
        UpdateBookInput input = new UpdateBookInput();
        input.setId(bookId);
        input.setIsbn("1234567890");
        input.setTitle("Updated Title");
        input.setGenre("Fiction");
        input.setDescription("Updated Description");
        input.setAuthor("Updated Author");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setIsbn("0987654321");
        existingBook.setTitle("Original Title");
        existingBook.setGenre("Non-Fiction");
        existingBook.setDescription("Original Description");
        existingBook.setAuthor("Original Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        UpdateBookOutput output = updateBookHandler.handle(input);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);

        assertNotNull(output);
        assertEquals(input.getIsbn(), existingBook.getIsbn());
        assertEquals(input.getTitle(), existingBook.getTitle());
        assertEquals(input.getGenre(), existingBook.getGenre());
        assertEquals(input.getDescription(), existingBook.getDescription());
        assertEquals(input.getAuthor(), existingBook.getAuthor());
    }
}