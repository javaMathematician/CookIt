package org.slovenlypolygon.recipes.backend.backendcards;

import android.content.Context;
import org.slovenlypolygon.recipes.backend.databaseutils.Deserializer;
import org.slovenlypolygon.recipes.backend.databaseutils.Dish;
import org.slovenlypolygon.recipes.frontend.frontendcards.StructuredCard;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardsFromIngredients {
    private final Context context;
    private Plug generator;
    private InputStream dishList;
    private InputStream photoMapper;
    private Map<String, String> mapper;

    public CardsFromIngredients(Context context) {
        this.context = context;
    }

    public void setDishList(InputStream dishList) {
        this.dishList = dishList;
        generator = new Plug(dishList);
    }

    public void setPhotoMapper(InputStream photoMapper) {
        this.photoMapper = photoMapper;

        try {
            mapper = Deserializer.deserializeIngredientPhotoMapper(photoMapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<StructuredCard> getCards() throws IOException {
        List<StructuredCard> views = new ArrayList<>(2000);

        Map<String, String> distinctIngredients = getDistinctIngredients();

        for (Map.Entry<String, String> entry : distinctIngredients.entrySet()) {
            StructuredCard child = new StructuredCard(context);
            child.setCardText(entry.getKey());
            child.setImageURL(entry.getValue());
            child.makeContent();

            views.add(child);
        }

        return views;
    }

    private Map<String, String> getDistinctIngredients() throws IOException {
        Map<String, String> map = new HashMap<>(2000);

        for (Dish dish : generator.getAll()) {
            map.putAll(cleanIngredients(dish.getRecipeIngredients()));
        }

        return map;
    }

    private Map<String, String> cleanIngredients(List<String> dirty) throws IOException {
        Map<String, String> cleaned = new HashMap<>(15);

        Pattern pattern = Pattern.compile("([\\d]+)|([—]{3})");
        Matcher matcher;

        for (String ingredient : dirty) {
            ingredient = ingredient
                    .replace("по вкусу", "")
                    .replace("щепотка", "")
                    .trim();

            matcher = pattern.matcher(ingredient);

            if (matcher.find() && matcher.start() > 0) {
                String substring = ingredient.substring(0, matcher.start() - 1).trim();

                if (substring.length() > 0) {
                    cleaned.put(substring, mapper.getOrDefault(substring, ""));
                }
            } else {
                cleaned.put(ingredient, mapper.getOrDefault(ingredient, ""));
            }
        }

        return cleaned;
    }
}
