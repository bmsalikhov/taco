package ru.bmsalikhov.taco.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bmsalikhov.taco.model.TacoOrder;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID> {}
