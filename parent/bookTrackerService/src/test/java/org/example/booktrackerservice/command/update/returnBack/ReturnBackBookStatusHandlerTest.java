package org.example.booktrackerservice.command.update.returnBack;

import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReturnBackBookStatusHandlerTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private ReturnBackBookStatusHandler returnBackBookStatusHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWhenBookIsDeleted() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsDeleted(true);

        ReturnBackBookStatusInput input = new ReturnBackBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));

        ReturnBackBookStatusOutput result = returnBackBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertTrue(result.getIsDeleted());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, never()).save(any(BookStatus.class));
    }

    @Test
    void testHandleWhenBookIsTaken() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsTaken(true);
        mockBookStatus.setIsDeleted(false);

        ReturnBackBookStatusInput input = new ReturnBackBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));
        when(bookStatusRepository.save(mockBookStatus)).thenReturn(mockBookStatus);

        ReturnBackBookStatusOutput result = returnBackBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertFalse(result.getIsTaken());
        assertNotNull(result.getReturned());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, times(1)).save(mockBookStatus);
    }

    @Test
    void testHandleWhenBookIsNotTaken() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsTaken(false);
        mockBookStatus.setIsDeleted(false);

        ReturnBackBookStatusInput input = new ReturnBackBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));

        ReturnBackBookStatusOutput result = returnBackBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertFalse(result.getIsTaken());
        assertNull(result.getReturned());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, never()).save(mockBookStatus);
    }
}
