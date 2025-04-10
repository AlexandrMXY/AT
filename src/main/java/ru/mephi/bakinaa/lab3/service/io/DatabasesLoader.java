package ru.mephi.bakinaa.lab3.service.io;

import ru.mephi.bakinaa.lab3.service.DatabaseService;

public interface DatabasesLoader {
    DatabaseService load();
    void save(DatabaseService databaseService);
}
