package ru.mephi.bakinaa.lab3.service;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.service.io.DatabasesLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DatabaseService {
    private DatabasesLoader loader;
    private final Map<String, Database> databases = new HashMap<>();

    public DatabaseService(DatabasesLoader loader) {
        this.loader = loader;
    }

    public Database getDatabase(String name) {
        return databases.get(name);
    }

    public void registerDatabase(Database database) {
        databases.put(database.getName(), database);
    }

    public Collection<Database> getDatabases() {
        return Collections.unmodifiableCollection(databases.values());
    }

    public void createDatabase(String name) {
        if (databases.containsKey(name))
            throw new InvalidDBAccessException("Database already exists");
        databases.put(name, new Database(name));
    }

    public void removeDatabase(String name) {
        if (!databases.containsKey(name))
            throw new InvalidDBAccessException("Database not found");
        databases.remove(name);
    }

    public void removeAll() {
        databases.clear();
    }

    @PreDestroy
    public void saveToStorage() {
        loader.save(this);
    }
}
