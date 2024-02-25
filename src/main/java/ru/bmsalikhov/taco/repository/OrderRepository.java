package ru.bmsalikhov.taco.repository;

import ru.bmsalikhov.taco.model.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
