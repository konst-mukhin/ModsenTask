package org.example.bookstorageservice.command;

import org.example.bookstorageservice.command.delete.DeleteBookHandler;
import org.example.bookstorageservice.command.delete.DeleteBookInput;
import org.example.bookstorageservice.command.delete.DeleteBookOutput;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class DeleteBookHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private DeleteBookHandler deleteBookHandler;

    public DeleteBookHandlerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldDeleteBookById() {
        Integer bookId = 1;
        DeleteBookInput input = new DeleteBookInput();
        input.setId(bookId);

        DeleteBookOutput output = deleteBookHandler.handle(input);

        verify(bookRepository, times(1)).deleteById(bookId);
        assertNotNull(output);
    }
}
