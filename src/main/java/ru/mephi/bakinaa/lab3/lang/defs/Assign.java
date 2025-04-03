package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@AllArgsConstructor
@Getter
public class Assign implements Definition {
    private Id left;
    private Expression right;
}
