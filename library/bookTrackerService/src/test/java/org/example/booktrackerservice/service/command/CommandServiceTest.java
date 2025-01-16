package org.example.booktrackerservice.service.command;

import org.example.booktrackerservice.dto.create.CreateBookStatusDto;
import org.example.booktrackerservice.dto.update.back.ReturnBackBookStatusDto;
import org.example.booktrackerservice.dto.update.take.TakeBookStatusDto;
import org.example.booktrackerservice.exception.NotFound;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandServiceTest {

    @InjectMocks
    private CommandService commandService;

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private ModelMapper modelMapper;

    private Message message;
    private BookStatus bookStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        message = new Message();
        message.setId(1);

        bookStatus = new BookStatus();
        bookStatus.setBookId(1);
        bookStatus.setIsTaken(false);
        bookStatus.setIsDeleted(false);
        bookStatus.setTaken(null);
        bookStatus.setReturned(null);
        bookStatus.setDeleted(null);
    }

    @Test
    void create_Success() {
        when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(bookStatus);
        when(modelMapper.map(bookStatus, CreateBookStatusDto.class)).thenReturn(new CreateBookStatusDto());

        CreateBookStatusDto result = commandService.create(message);

        assertNotNull(result);
        verify(bookStatusRepository, times(1)).save(any(BookStatus.class));
        verify(modelMapper, times(1)).map(bookStatus, CreateBookStatusDto.class);
    }

    @Test
    void delete_Success() {
        when(bookStatusRepository.findByBookId(message.getId())).thenReturn(Optional.of(bookStatus));

        commandService.delete(message);

        assertTrue(bookStatus.getIsDeleted());
        verify(bookStatusRepository, times(1)).save(bookStatus);
    }

    @Test
    void delete_NotFound() {
        when(bookStatusRepository.findByBookId(message.getId())).thenReturn(Optional.empty());

        commandService.delete(message);

        verify(bookStatusRepository, times(0)).save(any(BookStatus.class));
    }

    @Test
    void take_Success() throws NotFound {
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));
        when(modelMapper.map(bookStatus, TakeBookStatusDto.class)).thenReturn(new TakeBookStatusDto());

        TakeBookStatusDto result = commandService.take(bookStatus.getBookId());

        assertNotNull(result);
        assertTrue(bookStatus.getIsTaken());
        assertNotNull(bookStatus.getTaken());
        verify(bookStatusRepository, times(1)).save(bookStatus);
        verify(modelMapper, times(1)).map(bookStatus, TakeBookStatusDto.class);
    }

    @Test
    void take_NotFound() {
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> commandService.take(bookStatus.getBookId()));
        assertEquals("Wrong id", exception.getMessage());
    }

    @Test
    void take_IsDeleted() {
        bookStatus.setIsDeleted(true);
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        NotFound exception = assertThrows(NotFound.class, () -> commandService.take(bookStatus.getBookId()));
        assertEquals("This book was deleted", exception.getMessage());
    }

    @Test
    void returnBack_Success() throws NotFound {
        bookStatus.setIsTaken(true);
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));
        when(modelMapper.map(bookStatus, ReturnBackBookStatusDto.class)).thenReturn(new ReturnBackBookStatusDto());

        ReturnBackBookStatusDto result = commandService.returnBack(bookStatus.getBookId());

        assertNotNull(result);
        assertFalse(bookStatus.getIsTaken());
        assertNotNull(bookStatus.getReturned());
        verify(bookStatusRepository, times(1)).save(bookStatus);
        verify(modelMapper, times(1)).map(bookStatus, ReturnBackBookStatusDto.class);
    }

    @Test
    void returnBack_NotFound() {
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> commandService.returnBack(bookStatus.getBookId()));
        assertEquals("Wrong id", exception.getMessage());
    }

    @Test
    void returnBack_IsDeleted() {
        bookStatus.setIsDeleted(true);
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        NotFound exception = assertThrows(NotFound.class, () -> commandService.returnBack(bookStatus.getBookId()));
        assertEquals("This book was deleted", exception.getMessage());
    }
}
