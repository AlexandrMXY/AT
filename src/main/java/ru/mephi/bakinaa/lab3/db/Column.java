package ru.mephi.bakinaa.lab3.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Column {
    private String name;
    private int index = -1;
    private boolean nullable = true;
    private boolean primary = false;
    private boolean unique = false;

    public Column(String name) {
        this.name = name;
    }
}
