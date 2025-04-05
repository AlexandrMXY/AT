package ru.mephi.bakinaa.lab3.db.relations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.Id;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;


@Getter
@Setter
@AllArgsConstructor
public class JoinRowView implements RowView {
    private Join relation;
    private RowView left;
    private RowView right;

    @Override
    public SimpleObj get(Id id) {
        if (relation.getMapping().getIncompleteIdIndex(id) == Join.LEFT_MAPPING_ID)
            return left == null ? null : left.get(id);
        else
            return right == null ? null : right.get(id);
    }
}
