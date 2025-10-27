package com.team2.hrbank.employee.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Embeddable
public record EmployeeNumber(@Column(name = "employee_number", nullable = false, updatable = false) String value) {

    public static EmployeeNumber generate() {
        int year = LocalDate.now().getYear();
        long number = ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L);
        return generate(year, number);
    }

    public static EmployeeNumber generate(int year, Long number) {
        return new EmployeeNumber("EMP-%d-%d".formatted(year, number));
    }
}
