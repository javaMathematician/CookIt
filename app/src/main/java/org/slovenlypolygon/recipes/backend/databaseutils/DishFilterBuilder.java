package org.slovenlypolygon.recipes.backend.databaseutils;

import org.slovenlypolygon.recipes.backend.mainobjects.components.Category;
import org.slovenlypolygon.recipes.backend.mainobjects.Dish;
import org.slovenlypolygon.recipes.backend.mainobjects.components.PictureDishComponent;

import java.util.List;

public class DishFilterBuilder {
    private String name;
    private List<Dish> assortment;
    private List<Category> categories;
    private List<PictureDishComponent> dishComponents;

    public DishFilterBuilder setAssortment(List<Dish> assortment) {
        this.assortment = assortment;
        return this;
    }

    public DishFilterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DishFilterBuilder setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public DishFilterBuilder setDishComponents(List<PictureDishComponent> dishComponents) {
        this.dishComponents = dishComponents;
        return this;
    }

    public DishFilter createDishFilter() {
        DishFilter dishFilter = new DishFilter(assortment);
        dishFilter.setComponents(dishComponents);
        dishFilter.setName(name);

        return dishFilter;
    }
}