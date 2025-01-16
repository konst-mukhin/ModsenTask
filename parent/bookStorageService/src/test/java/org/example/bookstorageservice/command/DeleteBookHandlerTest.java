package org.example.bookstorageservice.command;

import org.example.bookstorageservice.command.delete.DeleteBookHandler;
import org.example.bookstorageservice.command.delete.DeleteBookInput;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class DeleteBookHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, Message> kafkaTemplate;

    @InjectMocks
    private DeleteBookHandler deleteBookHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle() {
        Integer bookId = 1;
        DeleteBookInput input = new DeleteBookInput();
        input.setId(bookId);

        deleteBookHandler.handle(input);

        verify(bookRepository, times(1)).deleteById(bookId);
        verify(kafkaTemplate, times(1)).send(eq("delete-topic"), any(Message.class));
    }
}
