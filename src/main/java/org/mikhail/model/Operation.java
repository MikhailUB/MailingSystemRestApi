package org.mikhail.model;

public enum Operation {
    REGISTRATION("регистрация"),
    DEPARTURE("убытие"),
    ARRIVAL("прибытие"),
    DELIVERY("вручение");

    private String name;
    Operation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
