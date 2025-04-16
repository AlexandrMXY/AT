package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;
import ru.mephi.bakinaa.lab3.util.MapRowView;
import ru.mephi.bakinaa.lab3.util.RelationRowComparator;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class JoinTest extends BaseTest {
    @Test
    public void join_trueCondition_returnAllPairs() {
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
    public void innerJoin_condition_returnAllCorrectPairs() {
        Relation rel = (Relation) perform("""
                X.join(Y, X::a == Y::a);
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .allSatisfy((row) -> {
                    assertThat(row.get(new Id("X", "a"))).isEqualTo(row.get(new Id("Y", "a")));
                });
    }

    @Test
    public void leftJoin_condition_returnAllCorrectPairs() {
        Relation rel = (Relation) perform("""
                X.leftJoin(Y, X::a == 1);
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .allSatisfy((row) -> {
                    if (!row.get(new Id("X", "a")).equals(new Int(1))) {
                        assertThat(row.get(new Id("Y", "a"))).isEqualTo(null);
                        assertThat(row.get(new Id("Y", "b"))).isEqualTo(null);
                    }
                });
    }

    @Test
    public void rightJoin_condition_returnAllCorrectPairs() {
        Relation rel = (Relation) perform("""
                X.rightJoin(Y, Y::a == 1);
                """, "test");

        assertThatIterable(rel)
                .usingElementComparator(new RelationRowComparator(rel))
                .allSatisfy((row) -> {
                    if (!new Int(1).equals(row.get(new Id("Y", "a")))) {
                        assertThat(row.get(new Id("X", "a"))).isNull();
                        assertThat(row.get(new Id("X", "b"))).isNull();
                    }
                });
    }

}
