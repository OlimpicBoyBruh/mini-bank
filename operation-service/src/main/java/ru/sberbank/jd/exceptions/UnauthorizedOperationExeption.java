package ru.sberbank.jd.exceptions;

public class UnauthorizedOperationExeption extends RuntimeException {

    public UnauthorizedOperationExeption(String message) {
        super(message);
    }
}
