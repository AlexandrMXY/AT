package ru.mephi.bakinaa.lab3.commons;

import lombok.experimental.UtilityClass;
import ru.mephi.bakinaa.lab3.commons.objects.Bool;

@UtilityClass
public class Functions {
    public static final Fun<Bool> AND = args -> Bool.of(((Bool)args[0]).value && ((Bool)args[1]).value );
    public static final Fun<Bool> OR  = args -> Bool.of(((Bool)args[0]).value || ((Bool)args[1]).value );
    public static final Fun<Bool> NOT = args -> Bool.of(!((Bool)args[0]).value);
}
