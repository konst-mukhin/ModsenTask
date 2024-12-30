package org.example.booktrackerservice.command.delete;

import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeleteBookStatusHandlerTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private DeleteBookStatusHandler deleteBookStatusHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStatus_BookStatusExists() {
        Message inputMessage = new Message();
        inputMessage.setId(1);

        BookStatus existingBookStatus = new BookStatus();
        existingBookStatus.setBookId(1);
        existingBookStatus.setIsDeleted(false);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(existingBookStatus));

        deleteBookStatusHandler.createStatus(inputMessage);

        assertTrue(existingBookStatus.getIsDeleted());
        assertNotNull(existingBookStatus.getDeleted());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, times(1)).save(existingBookStatus);
    }

    @Test
    void testCreateStatus_BookStatusDoesNotExist() {
        Message inputMessage = new Message();
        inputMessage.setId(2);

        when(bookStatusRepository.findByBookId(2)).thenReturn(Optional.empty());

        deleteBookStatusHandler.createStatus(inputMessage);

        verify(bookStatusRepository, times(1)).findByBookId(2);
        verify(bookStatusRepository, never()).save(any(BookStatus.class));
    }
}
