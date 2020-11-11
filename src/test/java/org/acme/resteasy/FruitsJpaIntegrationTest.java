package org.acme.resteasy;

import java.util.Optional;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.resteasy.domain.model.Fruit;
import org.acme.resteasy.infrastructure.persistence.mssql.JpaFruitRepository;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

@QuarkusTest
public class FruitsJpaIntegrationTest {

    @Inject
    JpaFruitRepository jpaFruitRepository;

    @Test
    public void test_jpaRepository() {
        Fruit fruit = Fruit.apple();
        jpaFruitRepository.saveNew(fruit);

        Optional<Fruit> retrievedFruit = jpaFruitRepository.fruitById(fruit.id);

        assertTrue(retrievedFruit.isPresent());
    }
}
