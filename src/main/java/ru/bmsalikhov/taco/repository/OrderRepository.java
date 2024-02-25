package ru.bmsalikhov.taco.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bmsalikhov.taco.model.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {}
