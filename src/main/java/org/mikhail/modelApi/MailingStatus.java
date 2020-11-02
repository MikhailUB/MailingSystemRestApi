package org.mikhail.modelApi;

public enum MailingStatus {
    NOT_FOUND("не найдено"),
    CREATED("создано"),
    IN_TRANSIT("в пути"),
    READY_TO_ISSUE("готов к выдаче"),
    DELIVERED("вручен");

    private String name;
    MailingStatus(String name) {
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
