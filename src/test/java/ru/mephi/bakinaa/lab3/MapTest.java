package ru.mephi.bakinaa.lab3;


import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.util.MapRowView;
import ru.mephi.bakinaa.lab3.util.RelationRowComparator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest extends BaseTest {
    @Test
    public void map_correctCols() {
        assertThatIterable(((Relation) perform("X.map(row { x = 1; y = 22; z = 3;});", "test")).getColumnsSet())
                .containsExactlyInAnyOrder(new Id("x"), new Id("y"), new Id("z"));
    }

    @Test
    public void map_correctValues() {
        Relation rel = (Relation) perform("""
                relationship Z {
                    Integer x;
                };
                Z.insert(row {
                    x = 1;
                });
                Z.insert(row {
                    x = 2;
                });
                Z.insert(row {
                    x = 1;
                });
                Z.map(row {
                    a = x + x * x;
                    b = x;
                    c = "777";
                });
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrder(
                        MapRowView.builder()
                                .obj("a", 2)
                                .obj("b", 1)
                                .obj("c", "777").build(),
                        MapRowView.builder()
                                .obj("a", 2)
                                .obj("b", 1)
                                .obj("c", "777").build(),
                        MapRowView.builder()
                                .obj("a", 6)
                                .obj("b", 2)
                                .obj("c", "777").build());
    }
}
