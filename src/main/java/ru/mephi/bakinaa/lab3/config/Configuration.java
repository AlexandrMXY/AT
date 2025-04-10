package ru.mephi.bakinaa.lab3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import ru.mephi.bakinaa.lab3.service.DatabaseService;
import ru.mephi.bakinaa.lab3.service.io.DatabasesLoader;
import ru.mephi.bakinaa.lab3.service.io.FilesDatabaseLoader;
import ru.mephi.bakinaa.lab3.service.io.StubDatabaseLoader;

@org.springframework.context.annotation.Configuration
@Slf4j
public class Configuration {
    @Bean
    public DatabaseService databaseService(DatabasesLoader loader) {
        return loader.load();
    }

    @Bean
    public DatabasesLoader databasesLoader(AppConfig config) {
        if (config.saveToFile() && config.databaseLocation() != null)
            return new FilesDatabaseLoader(config);
        if (config.saveToFile())
            log.atWarn()
                    .setMessage("Database files path not found. The database will not be saved after application shutdown.")
                    .log();
        return new StubDatabaseLoader();
    }
}
