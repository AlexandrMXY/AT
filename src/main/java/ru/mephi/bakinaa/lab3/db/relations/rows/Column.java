package ru.mephi.bakinaa.lab3.db.relations.rows;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.db.constrints.NotNullConstraint;
import ru.mephi.bakinaa.lab3.db.constrints.UniqueConstraint;
import ru.mephi.bakinaa.lab3.db.relations.Table;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;
import ru.mephi.bakinaa.lab3.lang.enums.Type;

import java.util.Set;


@AllArgsConstructor
public class Column {
    private final Table table;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Type type;
    @Getter @Setter
    private int index = -1;

    private String notNullConstraint = null;
    private String uniqueConstraint = null;

    public Column(Table table, String name, Type type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public boolean isNullable() {
        return notNullConstraint == null;
    }

    public void setNullable(boolean nullable) {
        if (isNullable() && !nullable) {
            String constraintName = name + "#notnull";
            table.addConstraint(new NotNullConstraint(constraintName, table, index));
            this.notNullConstraint = constraintName;
        } else if (!isNullable() && nullable) {
            table.removeConstraint(notNullConstraint);
            notNullConstraint = null;
        }
    }

    public boolean isUnique() {
        return uniqueConstraint != null;
    }

    public void setUnique(boolean unique) {
        if (!isUnique() && unique) {
            String constraintName = name + "#unique";
            table.addConstraint(new UniqueConstraint(constraintName, table, Set.of(index)));
            this.uniqueConstraint = constraintName;
        } else if (isUnique() && !unique) {
            table.removeConstraint(uniqueConstraint);
            uniqueConstraint = null;
        }
    }
}
