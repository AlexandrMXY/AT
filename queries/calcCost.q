Mapping
    .join(Component, componentId == Component::id)
    .group(bikeId, row {
        totalCost = reduce(0, __value + cnt * cost);
    });
