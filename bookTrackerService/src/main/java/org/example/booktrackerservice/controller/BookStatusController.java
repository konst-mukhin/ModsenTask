package org.example.booktrackerservice.controller;

import an.awesome.pipelinr.Pipeline;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.booktrackerservice.command.update.returnBack.ReturnBackBookStatusInput;
import org.example.booktrackerservice.command.update.returnBack.ReturnBackBookStatusOutput;
import org.example.booktrackerservice.command.update.take.TakeBookStatusInput;
import org.example.booktrackerservice.command.update.take.TakeBookStatusOutput;
import org.example.booktrackerservice.dto.GetBookStatusDto;
import org.example.booktrackerservice.dto.UpdateBookStatusDto;
import org.example.booktrackerservice.query.GetAllBookStatusesInput;
import org.example.booktrackerservice.query.GetAllBookStatusesOutput;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
@AllArgsConstructor
@Tag(name = "Book tracker", description = "API для взятия и возврата книг из библиотеки")
public class BookStatusController {
    private Pipeline pipeline;
    private final ModelMapper modelMapper;

    @Operation(summary = "Получить все свободные книги", description = "Возвращает список всех свободных книг",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен",
                            content = @Content(schema = @Schema(implementation = GetBookStatusDto[].class)))
            })
    @GetMapping("/all")
    public ResponseEntity<List<GetBookStatusDto>> getAll(GetAllBookStatusesInput getAllBookStatusesInput){
        List<GetAllBookStatusesOutput> getAllBooksOutputs = pipeline.send(getAllBookStatusesInput);
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(modelMapper.map(
                getAllBooksOutputs, GetBookStatusDto[].class
        )));
    }

    @Operation(summary = "Взять книгу", description = "Позволяет взять книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Книга успешно взята",
                            content = @Content(schema = @Schema(implementation = UpdateBookStatusDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/take/{id}")
    public ResponseEntity<UpdateBookStatusDto> take(@PathVariable Integer id){
        TakeBookStatusInput takeBookStatusInput = new TakeBookStatusInput();
        takeBookStatusInput.setBookId(id);
        TakeBookStatusOutput takeBookStatusOutput = pipeline.send(takeBookStatusInput);
        UpdateBookStatusDto updateBookStatusDto = modelMapper.map(takeBookStatusOutput, UpdateBookStatusDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateBookStatusDto);
    }

    @Operation(summary = "Вернуть книгу", description = "Позволяет вернуть книгу по ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Успешный возврат книги",
                            content = @Content(schema = @Schema(implementation = UpdateBookStatusDto.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена")
            })
    @PutMapping("/returnback/{id}")
    public ResponseEntity<UpdateBookStatusDto> returnBack(@PathVariable Integer id){
        ReturnBackBookStatusInput returnBackBookStatusInput = new ReturnBackBookStatusInput();
        returnBackBookStatusInput.setBookId(id);
        ReturnBackBookStatusOutput returnBackBookStatusOutput = pipeline.send(returnBackBookStatusInput);
        UpdateBookStatusDto updateBookStatusDto = modelMapper.map(returnBackBookStatusOutput, UpdateBookStatusDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateBookStatusDto);
    }
}
