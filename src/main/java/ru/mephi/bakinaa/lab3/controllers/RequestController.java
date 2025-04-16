package ru.mephi.bakinaa.lab3.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.utils.convertes.RelationResponseCsvHttpMessageConverter;
import ru.mephi.bakinaa.lab3.service.QueryProcessorService;

@RestController
@Slf4j
public class RequestController {
    private static final MediaType CSV = RelationResponseCsvHttpMessageConverter.CSV;

    @Autowired
    private QueryProcessorService queryProcessorService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<Obj> process(@RequestBody String body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(CSV)
                .body(queryProcessorService.executeGlobalQuery(body));
    }

    @PostMapping("/{database}")
    public ResponseEntity<Obj> process(@RequestBody String body, @PathVariable("database") String db) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(CSV)
                .body(queryProcessorService.executeDatabaseQuery(body, db));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        log.atInfo().setCause(e).log();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
