package ru.mephi.bakinaa.lab3;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class InvalidTableModificationTest extends BaseTest {
    @Test
    public void addColumns_duplicate_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addColumns({
                    String str;
                });
                """, "test");
        });
    }

    @Test
    public void addColumns_duplicatePrimary_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addColumns({
                    primary String str11;
                });
                """, "test");
        });
    }

    @Test
    public void addColumns_noColumns_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addColumn({
                });
                """, "test");
        });
    }

    @Test
    public void addColumn_constraint_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addColumn({
                    Unique(a) const;
                });
                """, "test");
        });
    }

    @Test
    public void editColumn_constraintInsteadCol_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.editColumn(a , {
                    Unique(a) const;
                });
                """, "test");
        });
    }

    @Test
    public void editColumn_pKeyDuplicate_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.editColumn(a , {
                    primary Integer a;
                });
                """, "test");
        });
    }

    @Test
    public void editColumn_multipleDefinitions_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.editColumn(a , {
                    Integer a;
                    Integer ab;
                });
                """, "test");
        });
    }

    @Test
    public void editColumn_duplicateName_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.editColumn(a , {
                    Integer b;
                });
                """, "test");
        });
    }

    @Test
    public void editColumn_editNotEmptyTable_throw() {
        perform("""
                A.insert(row {
                    id = 1;
                    a = 1;
                    b = 1;
                });
                """, "test");
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.editColumn(c , {
                    unique Integer c;
                });
                """, "test");
        });
    }

    @Test
    public void addConstraint_unknownCol_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addConstraint({
                    Unique(qew) c111;
                });
                """, "test");
        });
    }

    @Test
    public void addConstraint_duplicateName_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addConstraint({
                    Unique(a) con;
                });
                """, "test");
        });
    }

    @Test
    public void addConstraint_multipleConstraints_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.addConstraint({
                    Unique(b) c111;
                    Unique(a) c222;
                });
                """, "test");
        });
    }

    @Test
    public void removeColumns_volatilesConstraint_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.removeColumn(b);
                """, "test");
        });
    }

    @Test
    public void removeColumns_unknownCol_throw() {
        assertThrows(RuntimeException.class, () -> {
            perform("""
                A.removeColumn(qeqb);
                """, "test");
        });
    }
}
