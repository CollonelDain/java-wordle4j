package ru.yandex.practicum.exceptions;

public class DictionaryLoadingException extends Exception {
    public DictionaryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}