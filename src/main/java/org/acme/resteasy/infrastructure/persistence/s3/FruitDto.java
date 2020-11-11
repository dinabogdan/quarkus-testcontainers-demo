package org.acme.resteasy.infrastructure.persistence.s3;

import org.acme.resteasy.domain.model.Fruit;

public class FruitDto {

    public String id;
    public String name;
    public String color;

    public FruitDto() {

    }

    public FruitDto(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    static FruitDto from(Fruit fruit) {
        return new FruitDto(fruit.id, fruit.name, fruit.color);
    }
}
