package com.team2.hrbank.changelog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
public record EmployeeEmail(
        @Column(name = "email", nullable = false)
        String email
) {

    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public EmployeeEmail {
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다: " + email);
        }
    }

}
