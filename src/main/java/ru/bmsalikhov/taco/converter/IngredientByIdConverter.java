package ru.bmsalikhov.taco.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.bmsalikhov.taco.model.IngredientUDT;
import ru.bmsalikhov.taco.repository.IngredientRepository;
import ru.bmsalikhov.taco.utils.TacoUDRUtils;

import java.util.Objects;

@Component
public class IngredientByIdConverter implements Converter<String, IngredientUDT> {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientUDT convert(String id) {
        return TacoUDRUtils.toIngredientUDT(Objects.requireNonNull(ingredientRepository.findById(id).orElse(null)));
    }
}
