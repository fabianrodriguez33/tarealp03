package com.example.biblioteca.service.exception;


import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;

public class ServiceSqlException extends ConstraintViolationException {
    public ServiceSqlException(String message, SQLException root, String constraintName) {
        super(message, root, constraintName);
    }

    public ServiceSqlException(String message, SQLException root, String sql, String constraintName) {
        super(message, root, sql, constraintName);
    }
    @Override
    public SQLException getSQLException() {
        return super.getSQLException();
    }

    @Override
    public String getConstraintName() {
        return super.getConstraintName();
    }
}