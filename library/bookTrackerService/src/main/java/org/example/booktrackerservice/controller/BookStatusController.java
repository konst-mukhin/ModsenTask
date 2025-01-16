package org.example.booktrackerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.booktrackerservice.dto.get.GetBookStatusDto;
import org.example.booktrackerservice.dto.update.back.ReturnBackBookStatusDto;
import org.example.booktrackerservice.dto.update.take.TakeBookStatusDto;
import org.example.booktrackerservice.exception.NotFound;
import org.example.booktrackerservice.service.command.CommandService;
import org.example.booktrackerservice.service.query.QueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
@AllArgsConstructor
@Tag(name = "Book tracker", description = "API для взятия и возврата книг из библиотеки")
public class BookStatusController {
    private CommandService commandService;
    private QueryService queryService;

    @Operation(summary = "Получить все свободные книги", description = "Возвращает список всех свободных книг",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен",
                            content = @Content(schema = @Schema(implementation = GetBookStatusDto[].class)))
            })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<GetBookStatusDto>> getAll() throws NotFound {
        return ResponseEntity.status(HttpStatus.OK).body(queryService.getAll());
    }

    @Operation(summary = "Взять книгу", description = "Позволяет взять книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно взята",
                            content = @Content(schema = @Schema(implementation = TakeBookStatusDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/take/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TakeBookStatusDto> take(@PathVariable Integer id)
            throws NotFound{
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commandService.take(id));
    }

    @Operation(summary = "Вернуть книгу", description = "Позволяет вернуть книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Успешный возврат книги",
                            content = @Content(schema = @Schema(implementation = ReturnBackBookStatusDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/returnback/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReturnBackBookStatusDto> returnBack(@PathVariable Integer id) throws NotFound{
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commandService.returnBack(id));
    }
}
