package com.team2.hrbank.employee.domain;

import com.team2.hrbank.employee.domain.exception.EmployeeInvalidEmailException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record EmployeeEmail(@Column(name = "email", nullable = false) String value) {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    public EmployeeEmail {
        if (!emailPattern.matcher(value).matches()) {
            throw new EmployeeInvalidEmailException(value);
        }
    }
}
