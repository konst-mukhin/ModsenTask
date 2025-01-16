package org.example.bookstorageservice.query;

import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.query.getByIsbn.GetBookByIsbnHandler;
import org.example.bookstorageservice.query.getByIsbn.GetBookByIsbnInput;
import org.example.bookstorageservice.query.getByIsbn.GetBookByIsbnOutput;
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

public class GetBookByIsbnHandlerTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private GetBookByIsbnHandler getBookByIsbnHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldReturnBook_WhenBookExists() {
        String isbn = "1234567890";
        Book book = new Book(1, isbn, "Book Title", "Fiction", "Description", "Author");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        GetBookByIsbnInput input = new GetBookByIsbnInput();
        input.setIsbn(isbn);

        GetBookByIsbnOutput result = getBookByIsbnHandler.handle(input);

        verify(bookRepository, times(1)).findByIsbn(isbn);
        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        assertEquals("Book Title", result.getTitle());
        assertEquals("Fiction", result.getGenre());
        assertEquals("Description", result.getDescription());
        assertEquals("Author", result.getAuthor());
    }
}
