package org.example.bookstorageservice.controller;

import an.awesome.pipelinr.Pipeline;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.bookstorageservice.command.create.CreateBookInput;
import org.example.bookstorageservice.command.create.CreateBookOutput;
import org.example.bookstorageservice.command.delete.DeleteBookInput;
import org.example.bookstorageservice.command.update.UpdateBookInput;
import org.example.bookstorageservice.command.update.UpdateBookOutput;
import org.example.bookstorageservice.dto.BookDto;
import org.example.bookstorageservice.dto.CreateBookDto;
import org.example.bookstorageservice.dto.GetBookDto;
import org.example.bookstorageservice.dto.UpdateBookDto;
import org.example.bookstorageservice.query.getAll.GetAllBooksInput;
import org.example.bookstorageservice.query.getAll.GetAllBooksOutput;
import org.example.bookstorageservice.query.getById.GetBookByIdInput;
import org.example.bookstorageservice.query.getById.GetBookByIdOutput;
import org.example.bookstorageservice.query.getByIsbn.GetBookByIsbnInput;
import org.example.bookstorageservice.query.getByIsbn.GetBookByIsbnOutput;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@AllArgsConstructor
@Tag(name = "Book storage", description = "API для имитации библиотеки")
public class BookController {
    private Pipeline pipeline;
    private final ModelMapper modelMapper;

    @Operation(summary = "Создать книгу", description = "Создает новую книгу",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно создана",
                            content = @Content(schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping()
    public ResponseEntity<BookDto> create(@RequestBody CreateBookDto createBookDto){
        CreateBookInput createBookInput = modelMapper.map(createBookDto, CreateBookInput.class);
        CreateBookOutput createBookOutput = pipeline.send(createBookInput);
        BookDto bookDto = modelMapper.map(createBookOutput, BookDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
    }

    @Operation(summary = "Обновить книгу", description = "Обновляет данные книги по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно обновлена",
                            content = @Content(schema = @Schema(implementation = UpdateBookDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookDto> update(@PathVariable Integer id, @RequestBody UpdateBookDto updateBookDto){
        UpdateBookInput updateBookInput = modelMapper.map(updateBookDto, UpdateBookInput.class);
        updateBookInput.setId(id);
        UpdateBookOutput updateBookOutput = pipeline.send(updateBookInput);
        UpdateBookDto newCreateBookDto = modelMapper.map(updateBookOutput, UpdateBookDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(newCreateBookDto);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id){
        pipeline.send(new DeleteBookInput(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен",
                            content = @Content(schema = @Schema(implementation = GetBookDto[].class)))
            })
    @GetMapping("/all")
    public ResponseEntity<List<GetBookDto>> getAll(GetAllBooksInput getAllBooksInput){
        List<GetAllBooksOutput> getAllBooksOutputs = pipeline.send(getAllBooksInput);
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(modelMapper.map(
                getAllBooksOutputs, GetBookDto[].class
        )));
    }

    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга успешно получена",
                            content = @Content(schema = @Schema(implementation = GetBookDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @GetMapping("/{id}")
    public ResponseEntity<GetBookDto> getById(@PathVariable Integer id){
        GetBookByIdOutput getBookByIdOutput = pipeline.send(new GetBookByIdInput(id));
        return ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(getBookByIdOutput, GetBookDto.class));
    }

    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по её ISBN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга успешно получена",
                            content = @Content(schema = @Schema(implementation = GetBookDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<GetBookDto> getByIsbn(@PathVariable String isbn){
        GetBookByIsbnOutput getBookByIsbnOutput = pipeline.send(new GetBookByIsbnInput(isbn));
        return  ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(getBookByIsbnOutput, GetBookDto.class));
    }
}
