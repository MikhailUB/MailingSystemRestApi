package org.mikhail.model;

public enum  MailingType {
    LETTER("письмо"),
    PACKAGE("посылка"),
    BANDEROLE("бандероль"),
    POSTCARD("открытка");

    private String name;
    MailingType(String name) {
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
