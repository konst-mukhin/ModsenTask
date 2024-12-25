package org.example.bookstorageservice.query;

import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.query.getById.GetBookByIdHandler;
import org.example.bookstorageservice.query.getById.GetBookByIdInput;
import org.example.bookstorageservice.query.getById.GetBookByIdOutput;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetBookByIdHandlerTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private GetBookByIdHandler getBookByIdHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldReturnBook_WhenBookExists() {
        Integer bookId = 1;
        Book book = new Book(bookId, "1234567890", "Book Title", "Fiction", "Description", "Author");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        GetBookByIdInput input = new GetBookByIdInput();
        input.setId(bookId);

        GetBookByIdOutput result = getBookByIdHandler.handle(input);

        verify(bookRepository, times(1)).findById(bookId);
        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        assertEquals("Book Title", result.getTitle());
        assertEquals("Fiction", result.getGenre());
        assertEquals("Description", result.getDescription());
        assertEquals("Author", result.getAuthor());
    }
}
