package ru.mephi.bakinaa.lab3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.commons.Expressions;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

@Service
public class QueryProcessorService {
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private ParserService parserService;

    public Obj executeGlobalQuery(String query) {
        Expressions expressions = parserService.parse(query);
        ExpressionContext context = ExpressionContext.create(databaseService);
        return expressions.call(context);
    }

    public Obj executeDatabaseQuery(String query, String databaseName) {
        Database database = databaseService.getDatabase(databaseName);
        if (database == null)
            throw new InvalidDBAccessException("Unknown database " + databaseName);
        ExpressionContext context = ExpressionContext.create(database);
        Expressions expressions = parserService.parse(query);
        return expressions.call(context);
    }
}
