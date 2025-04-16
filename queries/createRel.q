relationship Component {
    primary Integer id;
    notnull String type;
    notnull String name;
    notnull Integer cost;
};

relationship Bicycle {
    primary Integer id;
    notnull String name;
    notnull String description;
};

relationship Mapping {
    Integer bikeId;
    Integer componentId;
    notnull Integer cnt;
    Foreign({bikeId -> Bicycle::id;}) bikeForeign;
    Foreign({componentId -> Component::id;}) componentForeign;
    Primary(bikeId, componentId) mappingPKey;
};



Component.insert(row {
    id = 1;
    type = "A";
    name = "C1";
    cost = 10;
});
Component.insert(row {
    id = 2;
    type = "A";
    name = "C2";
    cost = 20;
});
Component.insert(row {
    id = 3;
    type = "B";
    name = "C3";
    cost = 50;
});
Component.insert(row {
    id = 4;
    type = "B";
    name = "C4";
    cost = 40;
});
Component.insert(row {
    id = 5;
    type = "B";
    name = "C5";
    cost = 100;
});

Bicycle.insert(row {
    id = 1;
    name = "Bike 1";
    description = "Desc 1";
});
Bicycle.insert(row {
    id = 2;
    name = "Bike 2";
    description = "Desc 2";
});
Bicycle.insert(row {
    id = 3;
    name = "Bike 3";
    description = "Desc 3";
});

Mapping.insert(row {
    bikeId = 1;
    componentId = 1;
    cnt = 1;
});
Mapping.insert(row {
    bikeId = 1;
    componentId = 2;
    cnt = 30;
});
Mapping.insert(row {
    bikeId = 1;
    componentId = 4;
    cnt = 2;
});
Mapping.insert(row {
    bikeId = 2;
    componentId = 1;
    cnt = 1;
});
Mapping.insert(row {
    bikeId = 2;
    componentId = 3;
    cnt = 2;
});
Mapping.insert(row {
    bikeId = 2;
    componentId = 5;
    cnt = 2;
});
Mapping.insert(row {
    bikeId = 3;
    componentId = 5;
    cnt = 200;
});
