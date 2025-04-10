package ru.mephi.bakinaa.lab3.lang.defs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mephi.bakinaa.lab3.commons.objects.Id;

@AllArgsConstructor
@Getter
public class ForeignColReference implements Definition {
    private Id from;
    private Id to;
}
