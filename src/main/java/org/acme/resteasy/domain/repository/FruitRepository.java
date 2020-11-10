package org.acme.resteasy.domain.repository;

import java.util.Optional;

import org.acme.resteasy.domain.model.Fruit;

public interface FruitRepository {

    Optional<Fruit> fruitById(String id);

    void saveNew(Fruit fruit);
}
