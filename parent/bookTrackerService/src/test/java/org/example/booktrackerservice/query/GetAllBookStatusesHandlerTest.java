package org.example.booktrackerservice.query;

import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllBookStatusesHandlerTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private GetAllBookStatusesHandler getAllBookStatusesHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWithNonTakenBooks() {
        List<BookStatus> bookStatuses = new ArrayList<>();
        bookStatuses.add(createBookStatus(1, false, false));
        bookStatuses.add(createBookStatus(2, false, false));

        when(bookStatusRepository.findAll()).thenReturn(bookStatuses);

        GetAllBookStatusesInput query = new GetAllBookStatusesInput();

        List<GetAllBookStatusesOutput> result = getAllBookStatusesHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getBookId());
        assertEquals(2, result.get(1).getBookId());
        verify(bookStatusRepository, times(1)).findAll();
    }

    @Test
    void testHandleWithSomeTakenBooks() {
        List<BookStatus> bookStatuses = new ArrayList<>();
        bookStatuses.add(createBookStatus(1, false, false));
        bookStatuses.add(createBookStatus(2, true, false));
        bookStatuses.add(createBookStatus(3, false, false));

        when(bookStatusRepository.findAll()).thenReturn(bookStatuses);

        GetAllBookStatusesInput query = new GetAllBookStatusesInput();

        List<GetAllBookStatusesOutput> result = getAllBookStatusesHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getBookId());
        assertEquals(3, result.get(1).getBookId());
        verify(bookStatusRepository, times(1)).findAll();
    }

    @Test
    void testHandleWithNoBooks() {
        when(bookStatusRepository.findAll()).thenReturn(new ArrayList<>());

        GetAllBookStatusesInput query = new GetAllBookStatusesInput();

        List<GetAllBookStatusesOutput> result = getAllBookStatusesHandler.handle(query);

        assertTrue(result.isEmpty());
        verify(bookStatusRepository, times(1)).findAll();
    }

    private BookStatus createBookStatus(Integer bookId, boolean isTaken, boolean isDeleted) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setIsTaken(isTaken);
        bookStatus.setIsDeleted(isDeleted);
        return bookStatus;
    }
}

