package org.example.booktrackerservice.service.command;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.booktrackerservice.dto.create.CreateBookStatusDto;
import org.example.booktrackerservice.dto.update.back.ReturnBackBookStatusDto;
import org.example.booktrackerservice.dto.update.take.TakeBookStatusDto;
import org.example.booktrackerservice.exception.BookWasDeleted;
import org.example.booktrackerservice.exception.EntityNotFound;
import org.example.booktrackerservice.model.BookStatus;
import org.example.booktrackerservice.model.Message;
import org.example.booktrackerservice.repository.BookStatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CommandService {
    private final ModelMapper modelMapper;
    private BookStatusRepository bookStatusRepository;
    private final String kafkaCreateTopic = "create-topic";
    private final String kafkaGroupId = "group-id";
    private final String kafkaDeleteTopic = "delete-topic";

    @KafkaListener(topics = kafkaCreateTopic, groupId = kafkaGroupId)
    public CreateBookStatusDto create(Message message){
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(message.getId());
        bookStatus.setIsTaken(false);
        bookStatus.setIsDeleted(false);
        bookStatus.setTaken(null);
        bookStatus.setReturned(null);
        bookStatus.setDeleted(null);
        BookStatus createdBookStatus = bookStatusRepository.save(bookStatus);
        return modelMapper.map(createdBookStatus, CreateBookStatusDto.class);
    }

    @KafkaListener(topics = kafkaDeleteTopic, groupId = kafkaGroupId)
    public void delete(Message message){
        Optional<BookStatus> bookStatus = bookStatusRepository.findByBookId(message.getId());
        if(bookStatus.isPresent()) {
            bookStatus.get().setIsDeleted(true);
            bookStatus.get().setDeleted(LocalTime.now());
            bookStatusRepository.save(bookStatus.get());
        }
    }

    public TakeBookStatusDto take(Integer id)
            throws EntityNotFound, BookWasDeleted {
        BookStatus bookStatus = bookStatusRepository.findByBookId(id).orElseThrow(() -> new EntityNotFound("Wrong id"));
        if(bookStatus.getIsDeleted()) {
            throw new BookWasDeleted("This book was deleted");
        }
        if(!(bookStatus.getIsTaken())){
            bookStatus.setIsTaken(true);
            bookStatus.setTaken(LocalTime.now());
            bookStatus.setReturned(null);
            bookStatusRepository.save(bookStatus);
        }
        return modelMapper.map(bookStatus, TakeBookStatusDto.class);
    }

    public ReturnBackBookStatusDto returnBack(Integer id)
            throws EntityNotFound, BookWasDeleted {
        BookStatus bookStatus = bookStatusRepository.findByBookId(id).orElseThrow(() -> new EntityNotFound("Wrong id"));
        if(bookStatus.getIsDeleted()) {
            throw new BookWasDeleted("This book was deleted");
        }
        if(bookStatus.getIsTaken()) {
            bookStatus.setIsTaken(false);
            bookStatus.setReturned(LocalTime.now());
            bookStatusRepository.save(bookStatus);
        }
        return modelMapper.map(bookStatus, ReturnBackBookStatusDto.class);
    }
}
