package ru.mephi.bakinaa.lab3.db.registry;

import ru.mephi.bakinaa.lab3.commons.Fun;

import javax.annotation.Nullable;

public interface Registry {
    @Nullable Fun<?> getFunction(String name);
}
