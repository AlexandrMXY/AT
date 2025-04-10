package ru.mephi.bakinaa.lab3.service.io;

import ru.mephi.bakinaa.lab3.service.DatabaseService;

public class StubDatabaseLoader implements DatabasesLoader {
    @Override
    public DatabaseService load() {
        return new DatabaseService(this);
    }

    @Override
    public void save(DatabaseService databaseService) {

    }
}
