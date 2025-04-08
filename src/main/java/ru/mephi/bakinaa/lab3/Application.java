package ru.mephi.bakinaa.lab3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.db.registry.DefaultRegistry;
import ru.mephi.bakinaa.lab3.db.Database;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.lang.QueryParser;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
//        new Application().init();
    }

    private static final String q = """
            hashtable relationship A {
                primary Integer a;
                Integer b;
                Integer c;
                Integer d;
                Unique(d) feq;
            };
            A.insert(row {
                a = 1;
                b = 1;
                c = 1;
                d = 683;
            });
            A.insert(row {
                a = 2;
                b = 1;
                c = 1;
                d = 254;
            });
            A.insert(row {
                a = 3;
                b = 1;
                c = 1;
                d = 882;
            });
            A.insert(row {
                a = 4;
                b = 2;
                c = 1;
                d = 1645;
            });
            A.insert(row {
                a = 5;
                b = 2;
                c = 1;
                d = 423;
            });
            A.insert(row {
                a = 6;
                b = 2;
                c = 2;
                d = 123;
            });
            
            A.addColumns({
                Integer b1;
                Integer c1;
                Integer d1;
            });
            A.removeIf(true);
            A.addConstraint({
                Unique(b1, c1) unC;
            });
            A.editColumn(b1, {
                notnull unique Boolean bb1;
            });
            A.removeColumn(a);
            A.findAll();
            """;


//    @PostConstruct
    public void init() {
        var query = new QueryParser(q, new DefaultRegistry()).parse();
        Database database = new Database("db");

        Relation rel = (Relation)query.call(ExpressionContext.create(database, null));

        System.out.println(database);

        System.out.println(rel);
    }
}
