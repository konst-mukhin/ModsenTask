package org.example.booktrackerservice.command.create;

import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateBookStatusHandlerTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private CreateBookStatusHandler createBookStatusHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStatus() {
        Message inputMessage = new Message();
        inputMessage.setId(1);

        BookStatus mockBookStatus = new BookStatus();
        mockBookStatus.setBookId(1);
        mockBookStatus.setIsTaken(false);
        mockBookStatus.setIsDeleted(false);

        when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(mockBookStatus);

        var output = createBookStatusHandler.createStatus(inputMessage);

        assertEquals(1, output.getBookId());
        assertEquals(false, output.getIsTaken());
        assertEquals(false, output.getIsDeleted());
        verify(bookStatusRepository, times(1)).save(any(BookStatus.class));
    }
}
