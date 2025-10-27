package com.team2.hrbank.changelog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

@Entity
@Table(name = "employee_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @Deprecated)
public class EmployeeDetailLog {

    @Nullable
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeLogType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "change_log_id")
    private ChangeLog changeLog;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @Embedded
    private EmployeeEmail email;

    @Column(nullable = false)
    private String status;

    @Builder
    public EmployeeDetailLog(EmployeeLogType type, ChangeLog changeLog, LocalDate hireDate, String name,
                             String position, String department, EmployeeEmail email, String status) {
        this.type = type;
        this.changeLog = changeLog;
        this.hireDate = hireDate;
        this.name = name;
        this.position = position;
        this.department = department;
        this.email = email;
        this.status = status;
    }

}
