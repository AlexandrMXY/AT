package ru.mephi.bakinaa.lab3.db.views;

import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RowView {
    private final TablesView tables;
    private List<Integer> rows = new ArrayList<>();

    public RowView(TablesView tables) {
        this.tables = tables;
        rows = new ArrayList<>(tables.getTablesCnt());
        for (int i = 0; i < tables.getTablesCnt(); i++)
            rows.add(0);
    }

    public int getIndex(int rowIndex) {
        return rows.get(rowIndex);
    }

    public void setIndex(int rowIndex, int value) {
        rows.set(rowIndex, value);
    }

    public void incIndex(int rowIndex) {
        setIndex(rowIndex, getIndex(rowIndex) + 1);
    }

    public SimpleObj get(Id id) {
        var col = tables.getColumn(id);
        if (rows.get(col.getTableId()) < 0)
            return null;
        int tableRowIndex = rows.get(col.getTableId());
        return tables.getTable(col.getTableId()).getRow(tableRowIndex).get(col.getColumn().getIndex());
    }

    @Override
    public String toString() {
        return "[" + rows.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }
}
