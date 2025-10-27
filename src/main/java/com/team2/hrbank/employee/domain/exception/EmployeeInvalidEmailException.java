package com.team2.hrbank.employee.domain.exception;

public class EmployeeInvalidEmailException extends EmployeeException {

    public EmployeeInvalidEmailException(String email) {
        super("Invalid email format: '%s'".formatted(email));
    }
}
