package com.team2.hrbank.employee.domain;

import com.team2.hrbank.employee.domain.exception.EmployeeNameEmptyException;
import com.team2.hrbank.employee.domain.exception.EmployeePositionEmptyException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_employee_number", columnNames = "employee_number")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    @Embedded
    private EmployeeEmail email;

    @Embedded
    private EmployeeNumber employeeNumber;

    @Column(nullable = false)
    private Long departmentId;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    @Deprecated(since = "JPA 명세에 의해 필요함")
    protected Employee() {
    }

    private Employee(
            Long id,
            String name,
            EmployeeEmail email,
            EmployeeNumber employeeNumber,
            Long departmentId,
            String position,
            LocalDate hireDate,
            EmployeeStatus status
    ) {
        this.id = id;
        this.name = Employee.requireName(name.trim());
        this.email = email;
        this.employeeNumber = employeeNumber;
        this.departmentId = departmentId;
        this.position = Employee.requirePosition(position.trim());
        this.hireDate = hireDate;
        this.status = status;
    }

    private Employee(
            String name,
            EmployeeEmail email,
            EmployeeNumber employeeNumber,
            Long departmentId,
            String position,
            LocalDate hireDate,
            EmployeeStatus status
    ) {
        this(
                null,
                name,
                email,
                employeeNumber,
                departmentId,
                position,
                hireDate,
                status
        );
    }

    public static Employee from(EmployeeContext context) {
        return new Employee(
                Objects.requireNonNull(context.name()),
                Objects.requireNonNull(context.email()),
                EmployeeNumber.generate(),
                Objects.requireNonNull(context.departmentId()),
                Objects.requireNonNull(context.position()),
                Objects.requireNonNull(context.hireDate()),
                EmployeeStatus.ACTIVE
        );
    }

    public EmployeeContext save() {
        return new EmployeeContext()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .employeeNumber(this.employeeNumber)
                .departmentId(this.departmentId)
                .position(this.position)
                .hireDate(this.hireDate)
                .status(this.status);
    }

    public Employee load(EmployeeContext context) {
        this.name = context.name() == null ? this.name : Employee.requireName(context.name());
        this.email = context.email() == null ? this.email : context.email();
        this.employeeNumber = context.employeeNumber() == null ? this.employeeNumber : context.employeeNumber();
        this.departmentId = context.departmentId() == null ? this.departmentId : context.departmentId();
        this.position = context.position() == null ? this.position : Employee.requirePosition(context.position());
        this.hireDate = context.hireDate() == null ? this.hireDate : context.hireDate();
        this.status = context.status() == null ? this.status : context.status();
        return this;
    }

    public Long getId() {
        return id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    private static String requireName(String name) {
        if (name.isBlank()) {
            throw new EmployeeNameEmptyException();
        }
        return name;
    }

    private static String requirePosition(String position) {
        if (position.isBlank()) {
            throw new EmployeePositionEmptyException();
        }
        return position;
    }
}
