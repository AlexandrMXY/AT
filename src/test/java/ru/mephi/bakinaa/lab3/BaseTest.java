package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.controllers.RequestController;
import ru.mephi.bakinaa.lab3.service.DatabaseService;

@SpringBootTest
public abstract class BaseTest {
    @Autowired
    protected RequestController controller;
    @Autowired
    protected DatabaseService databaseService;

    @BeforeEach
    public void setupTestDatabase() {
        databaseService.removeAll();
        controller.process("createDatabase(test);");
        controller.process("""
                hashtable relationship A {
                    String str;
                    Boolean bool;
                    primary Integer id;
                    notnull Integer a;
                    unique Integer b;
                    Integer c;
                    Unique(b) con;
                };
                """, "test");

        controller.process("""
                relationship B {
                    Integer a;
                    Integer b;
                };
                """, "test");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                controller.process(String.format("""
                        B.insert(row {
                            a = %d;
                            b = %d;
                        });
                        """, i, j), "test");
            }
        }
        perform("""
                relationship X {
                    Integer a;
                    Integer b;
                };
                relationship Y {
                    Integer a;
                    Integer b;
                };
                X.insert(row {
                    a = 1;
                    b = 1;
                });
                X.insert(row {
                    a = 1;
                    b = 2;
                });
                X.insert(row {
                    a = 2;
                    b = 1;
                });
                X.insert(row {
                    a = 2;
                    b = 2;
                });
                Y.insert(row {
                    a = 1;
                    b = 1;
                });
                Y.insert(row {
                    a = 1;
                    b = 2;
                });
                Y.insert(row {
                    a = 2;
                    b = 1;
                });
                Y.insert(row {
                    a = 2;
                    b = 2;
                });
                """, "test");
    }

    protected Obj perform(String query, String db) {
        return controller.process(query, db).getBody();
    }
    protected Obj perform(String query) {
        return controller.process(query).getBody();
    }
}
