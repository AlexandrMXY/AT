package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.Int;
import ru.mephi.bakinaa.lab3.db.relations.Relation;
import ru.mephi.bakinaa.lab3.util.MapRowView;
import ru.mephi.bakinaa.lab3.util.RelationRowComparator;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class InsertDeleteTest extends BaseTest {
    @Test
    public void insert_valid_success() {
        Relation res = (Relation) perform("""
                A.insert(row {
                    id = 1;
                    a = 1;
                    b = 1;
                });
                A.findAll();
                """, "test");
        assertThatIterable(res)
            .usingElementComparator(new RelationRowComparator(res))
            .containsExactlyInAnyOrder(
                MapRowView.builder("A")
                        .obj("id", 1)
                        .obj("a", 1)
                        .obj("b", 1)
                        .build()
        );
    }

    @Test
    public void insert_2validRows_success() {
        Relation res = (Relation) perform("""
                A.insert(row {
                    id = 1;
                    a = 1;
                    b = 1;
                });
                A.insert(row {
                    str = "qe";
                    id = 2;
                    a = 2;
                    b = 2;
                });
                A.findAll();
                """, "test");
        assertThatIterable(res)
                .usingElementComparator(new RelationRowComparator(res))
                .containsExactlyInAnyOrder(
                        MapRowView.builder("A")
                                .obj("id", 1)
                                .obj("a", 1)
                                .obj("b", 1)
                                .build(),
                        MapRowView.builder("A")
                                .obj("id", 2)
                                .obj("a", 2)
                                .obj("b", 2)
                                .obj("str", "qe")
                                .build()
                );
    }

    @Test
    public void insert_invalidType_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.insert(row {
                    id = 1.0;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesNotNull_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.insert(row {
                    id = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesIdNotNull_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.insert(row {
                    a = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesUnique_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.insert(row {
                    id = 0;
                    a = 0;
                    b = 1;
                });
                A.insert(row {
                    id = 1;
                    a = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesIdUnique_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.insert(row {
                    id = 1;
                    a = 0;
                    b = 0;
                });
                A.insert(row {
                    id = 1;
                    a = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesGroupUnique_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Unique(a, b) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_volatilesGroupPKeyUnique_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Primary(a, b) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                    """, "test");
        });
    }

    @Test
    public void insert_noGroupUniqueViolation_success() {
        Relation res = (Relation) perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Unique(a, b) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.insert(row {
                    a = 1;
                    b = 2;
                });
                rel.insert(row {
                    a = 2;
                    b = 1;
                });
                rel.findAll();
                """, "test");
        assertThatIterable(res)
                .usingElementComparator(new RelationRowComparator(res))
                .containsExactlyInAnyOrder(
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 1)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 2)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("a", 2)
                                .obj("b", 1)
                                .build());

    }

    @Test
    public void insert_noGroupPrimaryViolation_success() {
        Relation res = (Relation) perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Primary(a, b) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.insert(row {
                    a = 1;
                    b = 2;
                });
                rel.insert(row {
                    a = 2;
                    b = 1;
                });
                rel.findAll();
                """, "test");
        assertThatIterable(res)
                .usingElementComparator(new RelationRowComparator(res))
                .containsExactlyInAnyOrder(
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 1)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 2)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("a", 2)
                                .obj("b", 1)
                                .build());

    }

    @Test
    public void insert_noGroupPrimaryNotNullViolation_success() {
        Relation res = (Relation) perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Unique(a, b) un;
                };
                rel.insert(row {
                    a = 1;
                });
                rel.insert(row {
                    b = 1;
                });
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.findAll();
                """, "test");
        assertThatIterable(res)
                .usingElementComparator(new RelationRowComparator(res))
                .containsExactlyInAnyOrder(
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("b", 1)
                                .build(),
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 1)
                                .build());
    }

    @Test
    public void removeBy_oneColSpecified_success() {
        Relation rel = (Relation) perform("""
                    B.removeBy(row {
                        a = 1;
                    });
                    B.findAll();
                """, "test");

        assertThatIterable(rel)
                .allSatisfy((row) -> {
                    assertNotEquals(new Int(1), row.get(new Id("B", "a")));
                });
    }

    @Test
    public void removeBy_allColsSpecified_success() {
        Relation rel = (Relation) perform("""
                    B.removeBy(row {
                        a = 1;
                        b = 2;
                    });
                    B.findAll();
                """, "test");

        assertThatIterable(rel)
                .allSatisfy((row) -> {
                    assertFalse(
                            Objects.equals(row.get(new Id("B", "a")), new Int(1)) &&
                                    Objects.equals(row.get(new Id("B", "b")), new Int(2))
                    );
                });
    }

    @Test
    public void removeIf_removeAll_success() {
        Relation rel = (Relation) perform("""
                    B.removeIf(true);
                    B.findAll();
                """, "test");
        assertThatIterable(rel).isEmpty();
    }

    @Test
    public void removeIf_condition_success() {
        Relation rel = (Relation) perform("""
                    B.removeIf(a > 5 && b < 5);
                    B.findAll();
                """, "test");
        assertThatIterable(rel).allSatisfy((row) -> {
            long a = ((Int)row.get(new Id("B", "a"))).value;
            long b = ((Int)row.get(new Id("B", "b"))).value;
            assertFalse(a > 5 && b < 5);
        });
    }

    @Test
    @Disabled
    public void insert_volatilesForeignKey_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Foreign({b -> B::a;}) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 100;
                });
                    """, "test");
        });
    }

    @Test
    @Disabled
    public void insert_validForeignKey_success() {
        Relation res = (Relation) perform("""
                
                relationship rel {
                    Integer a;
                    Integer b;
                    Foreign({b -> B::a;}) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                rel.findAll();
                """, "test");
        assertThatIterable(res)
                .usingElementComparator(new RelationRowComparator(res))
                .containsExactlyInAnyOrder(
                        MapRowView.builder("rel")
                                .obj("a", 1)
                                .obj("b", 1)
                                .build());

    }

    @Test
    @Disabled
    public void remove_volatilesForeignKey_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                relationship rel {
                    Integer a;
                    Integer b;
                    Foreign({b -> B::a;}) un;
                };
                rel.insert(row {
                    a = 1;
                    b = 1;
                });
                B.removeBy(row {
                    b = 1;
                });
                    """, "test");
        });

    }
}
