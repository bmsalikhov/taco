package ru.bmsalikhov.taco.repository;

import ru.bmsalikhov.taco.model.Ingredient;

import java.util.Optional;

public interface IngredientRepository {

    Iterable<Ingredient> findAll();
    Optional<Ingredient> finById(String id);
    Ingredient save(Ingredient ingredient);
}
