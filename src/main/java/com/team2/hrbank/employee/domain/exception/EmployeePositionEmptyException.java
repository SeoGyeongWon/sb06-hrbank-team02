package com.team2.hrbank.employee.domain.exception;

public class EmployeePositionEmptyException extends EmployeeException {

    public EmployeePositionEmptyException() {
        super( "Employee position is empty.");
    }
}
