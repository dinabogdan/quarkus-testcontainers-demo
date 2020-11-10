package org.acme.resteasy.infrastructure.persistence.mssql;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Fruit")
public class FruitEntity {

    @Id
    public String id;
    public String name;
    public String color;

    public FruitEntity() {

    }

    public FruitEntity(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
