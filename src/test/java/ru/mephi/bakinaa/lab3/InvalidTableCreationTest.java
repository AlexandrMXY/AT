package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class InvalidTableCreationTest extends BaseTest {
    @Test
    public void relationship_noCols_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {};
                """, "test");
        });
    }

    @Test
    public void indexedRelationship_noId_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                tree relationship rel {
                    Integer a;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_unknownColConstraint_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                relationship rel {
                    Integer a;
                    Unique(b) aa;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_duplicateCol_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    String a;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_duplicateConstraint_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    Unique(a) b;
                    Unique(a) b;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_duplicateRelation_throw() {
        perform("relationship rel { Integer a; };", "test");
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_scopedRelName_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship a::rel {
                    Integer a;
                    Unique(b) aa;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_scopedColName_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a::a;
                    Unique(b) aa;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_scopedConstraintName_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    Unique(b) aa::aa;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_multiplePKeysColCol_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    primary Integer a;
                    primary String aa;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_multiplePKeysColConstraint_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    primary Integer a;
                    String aa;
                    Primary(aa) pk;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_multiplePKeysConstraintConstraint_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    String aa;
                    Primary(a) pk1;
                    Primary(aa) pk2;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_foreignToUnknownTable_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    a -> qeqq::a;
                };
                """, "test");
        });
    }

    @Test
    public void relationship_foreignToUnknownCol_throw() {
        assertThrows(RuntimeException.class, () ->{
            perform("""
                relationship rel {
                    Integer a;
                    a -> A::qeqer;
                };
                """, "test");
        });
    }
}
