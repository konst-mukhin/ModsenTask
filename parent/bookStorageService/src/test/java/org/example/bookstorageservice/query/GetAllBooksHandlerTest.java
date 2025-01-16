package org.example.bookstorageservice.query;

import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.query.getAll.GetAllBooksHandler;
import org.example.bookstorageservice.query.getAll.GetAllBooksInput;
import org.example.bookstorageservice.query.getAll.GetAllBooksOutput;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetAllBooksHandlerTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private GetAllBooksHandler getAllBooksHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldReturnListOfBooks() {
        Book book1 = new Book(1, "1234567890", "Book Title 1", "Fiction", "Description 1", "Author 1");
        Book book2 = new Book(2, "0987654321", "Book Title 2", "Non-Fiction", "Description 2", "Author 2");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        GetAllBooksInput input = new GetAllBooksInput();

        List<GetAllBooksOutput> result = getAllBooksHandler.handle(input);

        verify(bookRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("1234567890", result.get(0).getIsbn());
        assertEquals("Book Title 1", result.get(0).getTitle());
        assertEquals("Fiction", result.get(0).getGenre());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals("Author 1", result.get(0).getAuthor());

        assertEquals("0987654321", result.get(1).getIsbn());
        assertEquals("Book Title 2", result.get(1).getTitle());
        assertEquals("Non-Fiction", result.get(1).getGenre());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals("Author 2", result.get(1).getAuthor());
    }
}
