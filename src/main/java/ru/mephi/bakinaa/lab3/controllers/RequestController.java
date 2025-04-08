package ru.mephi.bakinaa.lab3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mephi.bakinaa.lab3.service.QueryProcessorService;

@RestController
public class RequestController {
    @Autowired
    private QueryProcessorService queryProcessorService;

    @PostMapping("/")
    public void process(@RequestBody String body) {
        queryProcessorService.executeGlobalQuery(body);
    }

    @PostMapping("/{database}")
    public void process(@RequestBody String body, @PathVariable("database") String db) {
        queryProcessorService.executeDatabaseQuery(body, db);
    }
}
