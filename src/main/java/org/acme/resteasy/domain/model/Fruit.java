package org.acme.resteasy.domain.model;

import java.util.UUID;

public class Fruit {

    public String id;
    public String name;
    public String color;

    public Fruit(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Fruit apple() {
        return new Fruit(UUID.randomUUID().toString(), "Apple", "Red");
    }

    public static Fruit banana() {
        return new Fruit(UUID.randomUUID().toString(), "Banana", "Yellow");
    }

    public static Fruit orange() {
        return new Fruit(UUID.randomUUID().toString(), "Orange", "Orange");
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
