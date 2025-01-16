package org.example.booktrackerservice.service.query;

import org.example.booktrackerservice.dto.get.GetBookStatusDto;
import org.example.booktrackerservice.exception.NotFound;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QueryServiceTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private QueryService queryService;

    private BookStatus bookStatus1;
    private BookStatus bookStatus2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookStatus1 = new BookStatus(1, 1, false, false, null, null, null);
        bookStatus2 = new BookStatus(2, 2, true, false, null, null, null);
    }

    @Test
    void getAll_BooksAreAvailable() throws NotFound {
        when(bookStatusRepository.findAll()).thenReturn(Arrays.asList(bookStatus1, bookStatus2));
        when(modelMapper.map(any(BookStatus.class), eq(GetBookStatusDto.class)))
                .thenReturn(new GetBookStatusDto(1, 1, false, false, null, null, null));

        List<GetBookStatusDto> result = queryService.getAll();

        assert(result.size() == 1);
        assert(result.get(0).getId() == 1L);
        verify(bookStatusRepository, times(1)).findAll();
    }

    @Test
    void getAll_NotFound() {
        when(bookStatusRepository.findAll()).thenReturn(Arrays.asList());

        try {
            queryService.getAll();
        } catch (NotFound e) {
            assert(e.getMessage().equals("List is empty"));
        }
    }

    @Test
    void getAll_NoAvailableBooks() throws NotFound {
        when(bookStatusRepository.findAll()).thenReturn(Arrays.asList(bookStatus1, bookStatus2));
        when(modelMapper.map(any(BookStatus.class), eq(GetBookStatusDto.class)))
                .thenReturn(new GetBookStatusDto(1, 1, false, false, null, null, null));

        List<GetBookStatusDto> result = queryService.getAll();

        assert(result.size() == 1);
        verify(bookStatusRepository, times(1)).findAll();
    }
}
