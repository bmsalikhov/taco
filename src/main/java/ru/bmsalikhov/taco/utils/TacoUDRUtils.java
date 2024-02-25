package ru.bmsalikhov.taco.utils;

import ru.bmsalikhov.taco.model.Ingredient;
import ru.bmsalikhov.taco.model.IngredientUDT;
import ru.bmsalikhov.taco.model.Taco;
import ru.bmsalikhov.taco.model.TacoUDT;

public class TacoUDRUtils {
    public static IngredientUDT toIngredientUDT(Ingredient ingredient) {
        return new IngredientUDT(ingredient.getName(), ingredient.getType());
    }

    public static TacoUDT toTacoUDT(Taco taco) {
        return new TacoUDT(taco.getName(), taco.getIngredients());
    }
}
