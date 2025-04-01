package ru.mephi.bakinaa.lab3.db.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.db.core.Column;

@Getter
@AllArgsConstructor
public class ColumnView {
    private Column column;
    private int tableId;
    @Setter
    private boolean included = true;

    public ColumnView(Column column, int tableId) {
        this.column = column;
        this.tableId = tableId;
    }
}
