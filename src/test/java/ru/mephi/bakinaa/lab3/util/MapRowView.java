package ru.mephi.bakinaa.lab3.util;

import lombok.*;
import ru.mephi.bakinaa.lab3.commons.Obj;
import ru.mephi.bakinaa.lab3.commons.objects.*;
import ru.mephi.bakinaa.lab3.db.relations.rows.RowView;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@AllArgsConstructor
public class MapRowView implements RowView {
    private Map<Id, SimpleObj> objs = new HashMap<>();

    @Override
    public SimpleObj get(Id id) {
        return objs.get(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String defaultScope) {
        return new Builder(defaultScope);
    }

    public static int testComparator(RowView left, RowView right) {
        if (left instanceof MapRowView && !(right instanceof MapRowView))
            return -testComparator(right, left);
        if (right instanceof MapRowView row) {
            for (var key : row.objs.keySet()) {
                int cmp = SimpleObj.compare(left.get(key), row.get(key));
                if (cmp != 0)
                    return cmp;
            }
            return 0;
        } else throw new UnsupportedOperationException("At least one MapRowView expected as arg");
    }

    @NoArgsConstructor
    public static class Builder {
        private String scope;
        private Map<Id, SimpleObj> objs = new HashMap<>();

        public Builder(String scope) {
            this.scope = scope;
        }

        public MapRowView build() {
            return new MapRowView(objs);
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder obj(Id id, SimpleObj obj) {
            objs.put(id, obj);
            return this;
        }

        public Builder obj(String id, SimpleObj obj) {
            return obj(new Id(scope, id), obj);
        }

        public Builder obj(String id, String strVal) {
            return obj(id, new Str(strVal));
        }

        public Builder obj(String id, long iVal) {
            return obj(id, new Int(iVal));
        }

        public Builder obj(String id, double rVal) {
            return obj(id, new Real(rVal));
        }

        public Builder obj(String id, boolean bVal) {
            return obj(id, Bool.of(bVal));
        }
    }
}
