package ru.bmsalikhov.taco.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bmsalikhov.taco.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {}
