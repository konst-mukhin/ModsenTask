package org.example.bookstorageservice.service.command;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.bookstorageservice.dto.create.CreateBookDtoInput;
import org.example.bookstorageservice.dto.create.CreateBookDtoOutput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoInput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoOutput;
import org.example.bookstorageservice.exception.BadRequest;
import org.example.bookstorageservice.exception.NotFound;
import org.example.bookstorageservice.model.Book;
import org.example.bookstorageservice.model.Message;
import org.example.bookstorageservice.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CommandService {
    private final ModelMapper modelMapper;
    private BookRepository bookRepository;
    private KafkaTemplate<String, Message> kafkaTemplate;
    private static final String TOPIC_NAME_CREATE = "create-topic";
    private static final String TOPIC_NAME_DELETE = "delete-topic";

    public CreateBookDtoOutput create(CreateBookDtoInput createBookDtoInput) throws BadRequest {
        if (bookRepository.findByIsbn(createBookDtoInput.getIsbn()).isPresent()) {
            throw new BadRequest("Already exists. Change isbn");
        }
        Book book = modelMapper.map(createBookDtoInput, Book.class);
        Book createdBook = bookRepository.save(book);
        Message message = new Message(createdBook.getId());
        kafkaTemplate.send(TOPIC_NAME_CREATE, message);
        modelMapper.map(book, CreateBookDtoOutput.class);
        return modelMapper.map(book, CreateBookDtoOutput.class);
    }

    public UpdateBookDtoOutput update(Integer id, UpdateBookDtoInput updateBookDtoInput)
            throws NotFound {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new NotFound("Empty");
        }
        Book inputBook = modelMapper.map(updateBookDtoInput, Book.class);
        List<Book> books = bookRepository.findAll();
        for (Book checkBook : books) {
            if (checkBook.getIsbn().equals(inputBook.getIsbn())) {
                if(!checkBook.getIsbn().equals(book.get().getIsbn())) {
                    throw new NotFound("This isbn already exists");
                }
            }
        }
        book.get().setIsbn(inputBook.getIsbn());
        book.get().setTitle(inputBook.getTitle());
        book.get().setGenre(inputBook.getGenre());
        book.get().setDescription(inputBook.getDescription());
        book.get().setAuthor(inputBook.getAuthor());
        bookRepository.save(book.get());
        return modelMapper.map(book.get(), UpdateBookDtoOutput.class);
    }

    public void delete(Integer id) throws NotFound {
        if (bookRepository.findById(id).isEmpty()) {
            throw new NotFound("Wrong id");
        }
        bookRepository.deleteById(id);
        Message message = new Message(id);
        kafkaTemplate.send(TOPIC_NAME_DELETE, message);
    }
}
