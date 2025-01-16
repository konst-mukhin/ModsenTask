package org.example.bookstorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.bookstorageservice.dto.create.CreateBookDtoInput;
import org.example.bookstorageservice.dto.create.CreateBookDtoOutput;
import org.example.bookstorageservice.dto.get.GetBookDto;
import org.example.bookstorageservice.dto.update.UpdateBookDtoInput;
import org.example.bookstorageservice.dto.update.UpdateBookDtoOutput;
import org.example.bookstorageservice.exception.BadRequest;
import org.example.bookstorageservice.exception.NotFound;
import org.example.bookstorageservice.service.command.CommandService;
import org.example.bookstorageservice.service.query.QueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Book storage", description = "API для имитации библиотеки")
public class BookController {
    private CommandService commandService;
    private QueryService queryService;

    @Operation(summary = "Создать книгу", description = "Создает новую книгу",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно создана",
                            content = @Content(schema = @Schema(implementation = CreateBookDtoOutput.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping("/book")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CreateBookDtoOutput> create(@RequestBody CreateBookDtoInput createBookDtoInput)
        throws BadRequest {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.create(createBookDtoInput));
    }

    @Operation(summary = "Обновить книгу", description = "Обновляет данные книги по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно обновлена",
                            content = @Content(schema = @Schema(implementation = UpdateBookDtoOutput.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/book/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UpdateBookDtoOutput> update(@PathVariable Integer id, @RequestBody UpdateBookDtoInput updateBookDtoInput)
        throws NotFound {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commandService.update(id, updateBookDtoInput));
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @DeleteMapping("/book/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) throws NotFound {
        commandService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен",
                            content = @Content(schema = @Schema(implementation = GetBookDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Книги не найдены")
            })
    @GetMapping("/books")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<GetBookDto>> getAll() throws NotFound{
        return ResponseEntity.status(HttpStatus.OK).body(queryService.getAll());
    }

    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга успешно получена",
                            content = @Content(schema = @Schema(implementation = GetBookDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @GetMapping("/book/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<GetBookDto> getById(@PathVariable Integer id) throws NotFound {
        return ResponseEntity.status(HttpStatus.OK).body(queryService.getById(id));
    }

    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по её ISBN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга успешно получена",
                            content = @Content(schema = @Schema(implementation = GetBookDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @GetMapping("/book/isbn/{isbn}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<GetBookDto> getByIsbn(@PathVariable String isbn) throws NotFound {
        return  ResponseEntity.status(HttpStatus.OK).body(queryService.getByIsbn(isbn));
    }
}
