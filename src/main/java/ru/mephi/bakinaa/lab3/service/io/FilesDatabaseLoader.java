package ru.mephi.bakinaa.lab3.service.io;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.bakinaa.lab3.config.AppConfig;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.exceptions.DBException;
import ru.mephi.bakinaa.lab3.service.DatabaseService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Slf4j
public class FilesDatabaseLoader implements DatabasesLoader {
    private final AppConfig config;

    @Override
    public DatabaseService load() {
        Path databaseLocation = Path.of(config.databaseLocation());
        if (!Files.exists(databaseLocation) || !Files.isDirectory(databaseLocation))
            throw new DBException("Invalid database location");
        DatabaseService databaseService = new DatabaseService(this);

        for (File file : databaseLocation.toFile().listFiles()) {
            if (file.isDirectory())
                continue;
            if (!file.getName().endsWith(".db"))
                continue;
            Database loadedDb = loadDatabase(file);
            if (loadedDb == null)
                continue;
            databaseService.registerDatabase(loadedDb);
            log.info("Database {} loaded from {}", loadedDb.getName(), file.getName());
        }

        return databaseService;
    }

    private Database loadDatabase(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Database) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.atError()
                    .setCause(e)
                    .log("Unable to load database from file {}", file.getName());
            return null;
        }
    }

    private void saveDatabase(File file, Database database) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(database);
        } catch (IOException e) {
            log.atError()
                    .setCause(e)
                    .log("Unable to save database {} to file {}", database.getName(), file.getName());
        }
    }

    @Override
    public void save(DatabaseService databaseService) {
        Path databaseLocation = Path.of(config.databaseLocation());
        if (!Files.exists(databaseLocation) || !Files.isDirectory(databaseLocation))
            throw new DBException("Invalid database location");

        for (File file : databaseLocation.toFile().listFiles()) {
            if (file.isDirectory())
                continue;
            if (!file.getName().endsWith(".db"))
                continue;
            if (!file.delete())
                log.warn("Unable to delete file {}", file.getName());
        }

        for (Database database : databaseService.getDatabases()) {
            saveDatabase(
                    Paths.get(databaseLocation.toAbsolutePath().toString(), database.getName() + ".db").toFile(),
                    database);
        }
        log.info("Databases saved to {}", databaseLocation);
    }
}
