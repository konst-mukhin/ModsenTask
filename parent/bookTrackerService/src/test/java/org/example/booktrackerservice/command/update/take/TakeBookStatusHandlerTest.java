package org.example.booktrackerservice.command.update.take;

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

class TakeBookStatusHandlerTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private TakeBookStatusHandler takeBookStatusHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWhenBookIsDeleted() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsDeleted(true);

        TakeBookStatusInput input = new TakeBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));

        TakeBookStatusOutput result = takeBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertTrue(result.getIsDeleted());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, never()).save(any(BookStatus.class));
    }

    @Test
    void testHandleWhenBookIsNotTaken() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsTaken(false);
        mockBookStatus.setIsDeleted(false);

        TakeBookStatusInput input = new TakeBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));
        when(bookStatusRepository.save(mockBookStatus)).thenReturn(mockBookStatus);

        TakeBookStatusOutput result = takeBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertTrue(result.getIsTaken());
        assertNotNull(result.getTaken());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, times(1)).save(mockBookStatus);
    }

    @Test
    void testHandleWhenBookIsAlreadyTaken() {
        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsTaken(true);
        mockBookStatus.setIsDeleted(false);

        TakeBookStatusInput input = new TakeBookStatusInput();
        input.setBookId(1);

        when(bookStatusRepository.findByBookId(1)).thenReturn(Optional.of(mockBookStatus));

        TakeBookStatusOutput result = takeBookStatusHandler.handle(input);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertTrue(result.getIsTaken());
        assertNull(result.getTaken());
        verify(bookStatusRepository, times(1)).findByBookId(1);
        verify(bookStatusRepository, never()).save(any(BookStatus.class));
    }
}

