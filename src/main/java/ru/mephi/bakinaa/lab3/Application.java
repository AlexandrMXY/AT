package ru.mephi.bakinaa.lab3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.bakinaa.lab3.db.core.Database;
import ru.mephi.bakinaa.lab3.db.views.JoinType;
import ru.mephi.bakinaa.lab3.db.core.Table;
import ru.mephi.bakinaa.lab3.db.functions.filters.Condition;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.commons.objects.Str;
import ru.mephi.bakinaa.lab3.db.views.RowView;
import ru.mephi.bakinaa.lab3.db.views.TablesView;
import ru.mephi.bakinaa.lab3.lang.QueryParser;
import ru.mephi.bakinaa.lab3.lang.tree.Statements;
import ru.mephi.bakinaa.lab3.lang.tree.defs.TableDefinition;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.utils.GVUtils;
import ru.mephi.bakinaa.lab3.utils.MapBuilder;

import java.util.Objects;

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
            """;


    @PostConstruct
    public void init() {
        var res = new QueryParser(q).parse();
        GVUtils.save(res, "1.png");

        Database database = new Database("db");
        for (var statement : ((Statements)res).getStatements()) {
            database.createTable((TableDefinition) statement);
        }

        System.out.println(database);

        Table A = database.getTable("A");
        for (int i = 0; i < 20; i++) {
            A.insert(MapBuilder.<String, SimpleObj>builder()
                    .put("a", new Str("a" + i))
                    .put("b", new Int(i))
                    .put("c", new Str("c" + i))
                    .build());
        }
        Table B = database.getTable("B");
        for (int i = 0; i < 20; i++) {
            B.insert(MapBuilder.<String, SimpleObj>builder()
                    .put("a", new Str("a" + i))
                    .put("b", new Int(-i))
                    .put("c", new Str("c" + i))
                    .build());
        }

        TablesView tableView = new TablesView();
        tableView.join(A, JoinType.INNER, Condition.TRUE_CONDITION);
        tableView.join(B, JoinType.FULL, (table, row) -> {
            return Objects.equals(row.get(new Id("A", "a")), (row.get(new Id("B", "a")))) &&
                    ((Int)row.get(new Id("A", "b"))).value % 2 == 0;
        });

//        RowView row = tableView.first();
//        while (row != null) {
//            System.out.println(row);
//            row = tableView.next(row);
//        }
        tableView.limit(5);
        System.out.println(tableView.getResult().toString());
    }
}
