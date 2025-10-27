package com.team2.hrbank.employee.domain;

import java.time.LocalDate;

public final class EmployeeContext {

    private Long id;
    private String name;
    private EmployeeEmail email;
    private EmployeeNumber employeeNumber;
    private Long departmentId;
    private String position;
    private LocalDate hireDate;
    private EmployeeStatus status;

    public EmployeeContext id(Long id) {
        this.id = id;
        return this;
    }

    public EmployeeContext name(String name) {
        this.name = name;
        return this;
    }

    public EmployeeContext email(EmployeeEmail email) {
        this.email = email;
        return this;
    }

    public EmployeeContext employeeNumber(EmployeeNumber employeeNumber) {
        this.employeeNumber = employeeNumber;
        return this;
    }

    public EmployeeContext departmentId(Long departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public EmployeeContext position(String position) {
        this.position = position;
        return this;
    }

    public EmployeeContext hireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public EmployeeContext status(EmployeeStatus status) {
        this.status = status;
        return this;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public EmployeeEmail email() {
        return email;
    }

    public EmployeeNumber employeeNumber() {
        return employeeNumber;
    }

    public Long departmentId() {
        return departmentId;
    }

    public String position() {
        return position;
    }

    public LocalDate hireDate() {
        return hireDate;
    }

    public EmployeeStatus status() {
        return status;
    }
}
