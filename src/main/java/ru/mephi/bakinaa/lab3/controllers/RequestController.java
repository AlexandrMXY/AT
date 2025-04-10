package ru.mephi.bakinaa.lab3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.service.QueryProcessorService;

@RestController
public class RequestController {
    @Autowired
    private QueryProcessorService queryProcessorService;

    @PostMapping("/")
    public Obj process(@RequestBody String body) {
        return queryProcessorService.executeGlobalQuery(body);
    }

    @PostMapping("/{database}")
    public Obj process(@RequestBody String body, @PathVariable("database") String db) {
        return queryProcessorService.executeDatabaseQuery(body, db);
    }
}
