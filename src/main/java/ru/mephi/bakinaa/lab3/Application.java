package ru.mephi.bakinaa.lab3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.bakinaa.lab3.commons.ExpressionContext;
import ru.mephi.bakinaa.lab3.db.context.DefaultRegistry;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.lang.QueryParser;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        new Application().init();
    }

    private static final String q = """
            hashtable relationship A {
                notnull unique String a;
                primary Integer b;
                String c;
                Boolean d;
                Unique(c,d);
            };
            hashtable relationship B {
                notnull unique String a;
                primary Integer b;
                String c;
                Boolean d;
                Unique(c,d);
                a -> A::a;
            };
            A.insert(row {
                a = "sinx";
                b = 111;
                c = "dfe";
                d = 77 < 88 || true;
            });
            A.insert(row {
                a = "sinx1";
                b = 1111;
                c = "dfe1";
                d = 77 < 88 || true;
            });
            A.insert(row {
                a = "sinx11";
                b = 11111;
                c = "dfe11";
                d = 77 < 88 || true;
            });
            B.insert(row {
                a = "sinx";
                b = 222;
                c = "dfe2";
                d = 77 < 88 || true;
            });
            B.insert(row {
                a = "sinx1";
                b = 2222;
                c = "dfe22";
                d = 77 < 88 || true;
            });
            B.insert(row {
                a = "sinx11";
                b = 22222;
                c = "dfe222";
                d = 77 < 88 || true;
            });
            A.join(B, A::b < B::b);
            """;


    @PostConstruct
    public void init() {
        var query = new QueryParser(q, new DefaultRegistry()).parse();
        Database database = new Database("db");

        Relation rel = (Relation)query.call(ExpressionContext.create(database, null));

        System.out.println(database);

        System.out.println(rel);


//        Database database = new Database("db");
//        for (var statement : ((Statements)res).getStatements()) {
//            database.createTable((TableDefinition) statement);
//        }
//
//        System.out.println(database);
//
//        Table A = database.getTable("A");
//        for (int i = 0; i < 20; i++) {
//            A.insert(MapBuilder.<String, SimpleObj>builder()
//                    .put("a", new Str("a" + i))
//                    .put("b", new Int(i))
//                    .put("c", new Str("c" + i))
//                    .build());
//        }
//        Table B = database.getTable("B");
//        for (int i = 0; i < 20; i++) {
//            B.insert(MapBuilder.<String, SimpleObj>builder()
//                    .put("a", new Str("a" + i))
//                    .put("b", new Int(-i))
//                    .put("c", new Str("c" + i))
//                    .build());
//        }
//
//        TablesView tableView = new TablesView();
//        tableView.join(A, JoinType.INNER, Condition.TRUE_CONDITION);
//        tableView.join(B, JoinType.FULL, (table, row) -> {
//            return Objects.equals(row.get(new Id("A", "a")), (row.get(new Id("B", "a")))) &&
//                    ((Int)row.get(new Id("A", "b"))).value % 2 == 0;
//        });
//
////        RowView row = tableView.first();
////        while (row != null) {
////            System.out.println(row);
////            row = tableView.next(row);
////        }
//        tableView.limit(5);
//        System.out.println(tableView.getResult().toString());
    }
}
