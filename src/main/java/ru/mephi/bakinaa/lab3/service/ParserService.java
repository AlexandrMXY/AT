package ru.mephi.bakinaa.lab3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.bakinaa.lab3.commons.Expressions;
import ru.mephi.bakinaa.lab3.db.registry.Registry;
import ru.mephi.bakinaa.lab3.lang.QueryParser;

@Service
public class ParserService {
    @Autowired
    private Registry registry;
    public Expressions parse(String query) {
        QueryParser parser = new QueryParser(query, registry);
        return parser.parse();
    }
}
