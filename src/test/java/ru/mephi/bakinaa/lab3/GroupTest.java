package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.util.MapRowView;
import ru.mephi.bakinaa.lab3.util.RelationRowComparator;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class GroupTest extends BaseTest {
    @Test
    public void group_groupedCorrectly() {
        Relation rel = (Relation) perform("""
                relationship Z {
                    Integer a;
                    Integer b;
                };
                
                Z.insert(row {
                    a = 1;
                    b = 1;
                });
                Z.insert(row {
                    a = 1;
                    b = 1;
                });
                Z.insert(row {
                    a = 1;
                    b = 1;
                });
                Z.insert(row {
                    a = 1;
                    b = 2;
                });
                Z.insert(row {
                    a = 1;
                    b = 3;
                });
                Z.insert(row {
                    b = 1;
                });
                
                Z.group(a, b);
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrder(
                        MapRowView.builder()
                                .obj("a", 1)
                                .obj("b", 1).build(),
                        MapRowView.builder()
                                .obj("a", 1)
                                .obj("b", 2).build(),
                        MapRowView.builder()
                                .obj("a", 1)
                                .obj("b", 3).build(),
                        MapRowView.builder()
                                .obj("a", (SimpleObj) null)
                                .obj("b", 1).build());
    }

    @Test
    public void group_aggregationTest() {
        Relation rel = (Relation) perform("""
                relationship Z {
                    Integer a;
                    Integer b;
                };
                
                Z.insert(row {
                    a = 1;
                    b = 1;
                });
                Z.insert(row {
                    a = 1;
                    b = 1;
                });
                Z.insert(row {
                    a = 1;
                    b = 0;
                });
                Z.insert(row {
                    a = 1;
                    b = 2;
                });
                Z.insert(row {
                    a = 1;
                    b = 3;
                });
                Z.insert(row {
                    a = 1;
                    b = null;
                });
                
                Z.group(a, row {
                    sz = groupSize();
                    mn = min(b);
                    mx = max(b);
                    sm = sum(b);
                    smr = reduce(0, __value + b);
                    mul = reduce(1, __value * b);
                });
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrder(
                        MapRowView.builder()
                                .obj("a", 1)
                                .obj("sz", 6)
                                .obj("mn", 0)
                                .obj("mx", 3)
                                .obj("sm", 7)
                                .obj("smr", 7)
                                .obj("mul", 0).build());

    }
}
