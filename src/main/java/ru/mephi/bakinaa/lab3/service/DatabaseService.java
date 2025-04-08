package ru.mephi.bakinaa.lab3.service;

import org.springframework.stereotype.Service;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashMap;
import java.util.Map;

@Service
public class DatabaseService {
    private final Map<String, Database> databases = new HashMap<>();

    public Database getDatabase(String name) {
        return databases.get(name);
    }

    public void createDatabase(String name) {
        if (databases.containsKey(name))
            throw new InvalidDBAccessException("Database already exists");
        databases.put(name, new Database(name));
    }

    public void removeDatabase(String name) {
        if (!databases.containsKey(name))
            throw new InvalidDBAccessException("Database already exists");
        databases.remove(name);
    }
}
