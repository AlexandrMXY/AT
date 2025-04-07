package ru.mephi.bakinaa.lab3.db.relations.rows;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.lang.enums.Type;

@Getter
@Setter
@AllArgsConstructor
public class Column {
    private String name;
    private Type type;
    private int index = -1;
    private boolean nullable = true;
    private boolean primary = false;
    private boolean unique = false;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}
