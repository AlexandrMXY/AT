package ru.mephi.bakinaa.lab3.commons;

import ru.mephi.bakinaa.lab3.commons.objects.Id;

public record Sort(
        Order order,
        Id row
) implements Obj {
    public enum Order {
        ASC,
        DESC
    }
}
