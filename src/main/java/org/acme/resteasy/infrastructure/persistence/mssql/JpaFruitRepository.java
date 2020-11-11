package org.acme.resteasy.infrastructure.persistence.mssql;

import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.acme.resteasy.domain.model.Fruit;
import org.acme.resteasy.domain.repository.FruitRepository;

@Dependent
public class JpaFruitRepository implements FruitRepository {
    
    private EntityManager em;

    public JpaFruitRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Optional<Fruit> fruitById(String id) {
        return Optional.ofNullable(em.find(FruitEntity.class, id)).map(this::mapToFruit);
    }

    @Override
    @Transactional
    public void saveNew(Fruit fruit) {
        FruitEntity entity = new FruitEntity(fruit.id, fruit.name, fruit.color);
        em.persist(entity);
    }

    private Fruit mapToFruit(FruitEntity fruitEntity) {
        return new Fruit(fruitEntity.id, fruitEntity.name, fruitEntity.color);
    }
}
