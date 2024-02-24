package ru.sberbank.api.account.service;

public enum Status {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    CLOSED("CLOSED");

    private final String statusString;

    Status(String statusString) {
        this.statusString = statusString;
    }

    @Override
    public String toString() {
        return statusString;
    }
}

