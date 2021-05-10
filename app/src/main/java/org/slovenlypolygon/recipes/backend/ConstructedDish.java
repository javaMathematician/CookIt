package org.slovenlypolygon.recipes.backend;

import org.slovenlypolygon.recipes.backend.mergedpojos.DishWithComponents;
import org.slovenlypolygon.recipes.backend.rawobjects.RawDirtyComponent;
import org.slovenlypolygon.recipes.backend.rawobjects.RawStep;

import java.util.List;

public class ConstructedDish {
    private List<RawStep> rawSteps;
    private DishWithComponents dishWithComponents;
    private List<RawDirtyComponent> rawDirtyComponents;

    public List<RawStep> getRawSteps() {
        return rawSteps;
    }

    public void setRawSteps(List<RawStep> rawSteps) {
        this.rawSteps = rawSteps;
    }

    public DishWithComponents getDishWithComponents() {
        return dishWithComponents;
    }

    public void setDishWithComponents(DishWithComponents dishWithComponents) {
        this.dishWithComponents = dishWithComponents;
    }

    public List<RawDirtyComponent> getRawDirtyComponents() {
        return rawDirtyComponents;
    }

    public void setRawDirtyComponents(List<RawDirtyComponent> rawDirtyComponents) {
        this.rawDirtyComponents = rawDirtyComponents;
    }
}
