package com.example.map211psvm.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
