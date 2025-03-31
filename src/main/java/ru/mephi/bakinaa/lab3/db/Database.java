package ru.mephi.bakinaa.lab3.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Database {
    private final String name;
    private final Map<String, Table> tables = new HashMap<>();
}
