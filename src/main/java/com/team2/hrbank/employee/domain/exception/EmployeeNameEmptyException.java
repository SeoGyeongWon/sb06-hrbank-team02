package com.team2.hrbank.employee.domain.exception;

public class EmployeeNameEmptyException extends EmployeeException {

    public EmployeeNameEmptyException() {
        super( "Employee name is empty.");
    }
}
