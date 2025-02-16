package org.example.bookstorageservice.service.command;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.bookstorageservice.dto.create.CreateBookDtoInput;
import org.example.bookstorageservice.dto.create.CreateBookDtoOutput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoInput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoOutput;
import org.example.bookstorageservice.exception.IsbnAlreadyExists;
import org.example.bookstorageservice.exception.EntityNotFound;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CommandService {
    private final ModelMapper modelMapper;
    private BookRepository bookRepository;
    private KafkaTemplate<String, Message> kafkaTemplate;
    private static final String TOPIC_NAME_CREATE = "create-topic";
    private static final String TOPIC_NAME_DELETE = "delete-topic";

    public CreateBookDtoOutput create(CreateBookDtoInput createBookDtoInput) throws IsbnAlreadyExists {
        if (bookRepository.findByIsbn(createBookDtoInput.getIsbn()).isPresent()) {
            throw new IsbnAlreadyExists("Already exists. Change isbn");
        }
        Book book = modelMapper.map(createBookDtoInput, Book.class);
        Book createdBook = bookRepository.save(book);
        Message message = new Message(createdBook.getId());
        kafkaTemplate.send(TOPIC_NAME_CREATE, message);
        modelMapper.map(book, CreateBookDtoOutput.class);
        return modelMapper.map(book, CreateBookDtoOutput.class);
    }

    public UpdateBookDtoOutput update(Integer id, UpdateBookDtoInput updateBookDtoInput)
            throws IsbnAlreadyExists, EntityNotFound {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFound("Empty"));
        Book inputBook = modelMapper.map(updateBookDtoInput, Book.class);
        List<Book> books = bookRepository.findAll();
        for (Book checkBook : books) {
            if (checkBook.getIsbn().equals(inputBook.getIsbn())) {
                if(!checkBook.getIsbn().equals(book.getIsbn())) {
                    throw new IsbnAlreadyExists("This isbn already exists");
                }
            }
        }
        book.setIsbn(inputBook.getIsbn());
        book.setTitle(inputBook.getTitle());
        book.setGenre(inputBook.getGenre());
        book.setDescription(inputBook.getDescription());
        book.setAuthor(inputBook.getAuthor());
        bookRepository.save(book);
        return modelMapper.map(book, UpdateBookDtoOutput.class);
    }

    public void delete(Integer id) throws EntityNotFound {
        bookRepository.findById(id).orElseThrow(() -> new EntityNotFound("Wrong id"));
        bookRepository.deleteById(id);
        Message message = new Message(id);
        kafkaTemplate.send(TOPIC_NAME_DELETE, message);
    }
}
