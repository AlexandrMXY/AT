package ru.mephi.bakinaa.lab3.db.relations;

import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.exceptions.InvalidDBAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleRowMapping implements RowMapping {
    private final Map<Id, Integer> mapping = new HashMap<>();
    private final Map<String, Integer> inaccurateMapping = new HashMap<>();

    public void register(Id id, int value) {
        if (mapping.containsKey(id))
            throw new InvalidDBAccessException("Mapping error: already mapped");

        mapping.put(id, value);

        if (inaccurateMapping.containsKey(id.value))
            inaccurateMapping.remove(id.value);
        else
            inaccurateMapping.put(id.value, value);;
    }

    @Override
    public int getIncompleteIdIndex(Id id) {
        Integer res;
        if (id.scope == null)
            res = inaccurateMapping.get(id.value);
        else
            res = mapping.get(id);
        if (res == null)
            throw new InvalidDBAccessException("Unable to find column with id " + id + " or id is ambiguous");
        return res;
    }

    @Override
    public Set<Id> getColumns() {
        return mapping.keySet();
    }
}
