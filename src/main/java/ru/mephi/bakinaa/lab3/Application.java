package ru.mephi.bakinaa.lab3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.bakinaa.lab3.lang.QueryParser;
import ru.mephi.bakinaa.lab3.utils.GVUtils;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        new Application().init();
    }

    private static final String q = """
            hashtable relationship rel {
                notnull unique unique String aaa;
                primary Integer bbb;
                String qq;
                Boolean bbb;
                Primary(a,b,d);
                Unique(c,d,de);
                aa -> v::de;
            };
            aaa.bbb(x).cc::dd;
            aaa(row {
                x=y;
                y=z;
            });
            """;

    public static final String q1 = """
            aaa.bbb(x).cc::dd;
            """;

    @PostConstruct
    public void init() {
        var res = new QueryParser(q).parse();
        GVUtils.save(res, "1.png");
    }
}
