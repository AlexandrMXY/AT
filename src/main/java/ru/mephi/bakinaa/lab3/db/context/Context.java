package ru.mephi.bakinaa.lab3.db.context;

import ru.mephi.bakinaa.lab3.commons.Fun;

import javax.annotation.Nullable;

public interface Context {
    @Nullable Fun<?> getFunction(String name);
}
