package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.util.MapRowView;
import ru.mephi.bakinaa.lab3.util.RelationRowComparator;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class JoinTest extends BaseTest {
    @Test
    public void innerJoin_trueCondition_returnAllPairs() {
        Relation rel = (Relation) perform("""
                X.join(Y, true);
                """, "test");

        List<RowView> expected = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                for (int i1 = 1; i1 <= 2; i1++) {
                    for (int j1 = 1; j1 <= 2; j1++) {
                        expected.add(
                                MapRowView.builder("X")
                                        .obj("a", i)
                                        .obj("b", j)
                                        .setScope("Y")
                                        .obj("a", i1)
                                        .obj("b", j1)
                                        .build()
                        );
                    }
                }
            }
        }
        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void leftJoin_trueCondition_returnAllPairs() {
        Relation rel = (Relation) perform("""
                X.leftJoin(Y, true);
                """, "test");

        List<RowView> expected = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                for (int i1 = 1; i1 <= 2; i1++) {
                    for (int j1 = 1; j1 <= 2; j1++) {
                        expected.add(
                                MapRowView.builder("X")
                                        .obj("a", i)
                                        .obj("b", j)
                                        .setScope("Y")
                                        .obj("a", i1)
                                        .obj("b", j1)
                                        .build());
                    }
                }
                expected.add(MapRowView.builder("X")
                        .obj("a", i)
                        .obj("b", j).build());
            }
        }
        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void rightJoin_trueCondition_returnAllPairs() {
        Relation rel = (Relation) perform("""
                X.rightJoin(Y, true);
                """, "test");

        List<RowView> expected = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                for (int i1 = 1; i1 <= 2; i1++) {
                    for (int j1 = 1; j1 <= 2; j1++) {
                        expected.add(
                                MapRowView.builder("X")
                                        .obj("a", i)
                                        .obj("b", j)
                                        .setScope("Y")
                                        .obj("a", i1)
                                        .obj("b", j1)
                                        .build());
                        if (i == 1 && j == 1)
                            expected.add(MapRowView.builder("Y")
                                    .obj("a", i1)
                                    .obj("b", j1).build());
                    }
                }

            }
        }
        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void fullJoin_trueCondition_returnAllPairs() {
        Relation rel = (Relation) perform("""
                X.fullJoin(Y, true);
                """, "test");

        List<RowView> expected = new ArrayList<>();
        expected.add(MapRowView.builder().build());
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                for (int i1 = 1; i1 <= 2; i1++) {
                    for (int j1 = 1; j1 <= 2; j1++) {
                        expected.add(
                                MapRowView.builder("X")
                                        .obj("a", i)
                                        .obj("b", j)
                                        .setScope("Y")
                                        .obj("a", i1)
                                        .obj("b", j1)
                                        .build());
                        if (i == 1 && j == 1)
                            expected.add(MapRowView.builder("Y")
                                    .obj("a", i1)
                                    .obj("b", j1).build());
                    }
                }
                expected.add(MapRowView.builder("X")
                        .obj("a", i)
                        .obj("b", j).build());
            }
//            expected.add(MapRowView.builder().build());
        }
        rel.toString();
        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .containsExactlyInAnyOrderElementsOf(expected);
    }
}
