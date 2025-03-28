package ru.mephi.bakinaa.lab3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.bakinaa.lab3.lang.QueryParser;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        new Application().init();
    }

    @PostConstruct
    public void init() {
        new QueryParser("fn(\"aaa\", fn(false, 22 || 55) && 8 && 9, -12 <= 4, !null, -5.4)").parse();
    }
}
