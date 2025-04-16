package ru.mephi.bakinaa.lab3;


import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Relation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class SortTest extends BaseTest {
    @Test
    public void sort_asc_shouldSortCorrectly() {
        Relation rel = (Relation) perform("""
                relationship Z {
                    Integer x;
                };
                Z.insert(row {
                    x = 1;
                });
                Z.insert(row {
                    x = 77;
                });
                Z.insert(row {
                    x = 6;
                });
                Z.sort(asc x);
                """, "test");

        for (int i = 1; i < rel.getSize(); i++) {
            assertTrue(SimpleObj.compare(rel.get(i - 1, new Id("x")), rel.get(i, new Id("x"))) <= 0);
        }
    }

    @Test
    public void sort_desc_shouldSortCorrectly() {
        Relation rel = (Relation) perform("""
                relationship Z {
                    Integer x;
                };
                Z.insert(row {
                    x = 1;
                });
                Z.insert(row {
                    x = 77;
                });
                Z.insert(row {
                    x = 6;
                });
                Z.sort(desc x);
                """, "test");

        assertEquals(3, rel.getSize());
        for (int i = 1; i < rel.getSize(); i++) {
            assertTrue(SimpleObj.compare(rel.get(i - 1, new Id("x")), rel.get(i, new Id("x"))) >= 0);
        }
    }
}
