package com.team2.hrbank.employee;

import com.team2.hrbank.changelog.dto.EmployeeLogDto;
import com.team2.hrbank.employee.domain.Employee;
import com.team2.hrbank.employee.domain.EmployeeContext;
import com.team2.hrbank.employee.domain.EmployeeEmail;
import com.team2.hrbank.employee.domain.EmployeeStatus;
import com.team2.hrbank.employee.dto.EmployeeCreateRequest;
import com.team2.hrbank.employee.dto.EmployeeDto;
import com.team2.hrbank.employee.dto.EmployeeUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    default EmployeeContext toEntityContext(EmployeeCreateRequest employee) {
        return new EmployeeContext()
                .name(employee.name())
                .email(new EmployeeEmail(employee.email()))
                .departmentId(employee.departmentId())
                .position(employee.position())
                .hireDate(employee.hireDate());
    }

    default EmployeeContext toEntityContext(EmployeeUpdateRequest employee) {
        return new EmployeeContext()
                .name(employee.name())
                .email(new EmployeeEmail(employee.email()))
                .departmentId(employee.departmentId())
                .position(employee.position())
                .hireDate(employee.hireDate())
                .status(EmployeeStatus.valueOf(employee.status()));
    }

    default EmployeeDto toDto(Employee employee, String departmentName, Long profileImageId) {
        EmployeeContext context = employee.save();
        return new EmployeeDto(
                context.id(),
                context.name(),
                context.email().value(),
                context.employeeNumber().value(),
                context.departmentId(),
                departmentName,
                context.position(),
                context.hireDate(),
                context.status().getLabel(),
                profileImageId
        );
    }

    default EmployeeLogDto.CreateLogRequest toChangeLogRequest(
            EmployeeContext employeeContext,
            String departmentName,
            String memo,
            String ipAddress
    ) {
        return new EmployeeLogDto.CreateLogRequest(
                employeeContext.hireDate(),
                employeeContext.name(),
                employeeContext.position(),
                departmentName,
                employeeContext.email().value(),
                employeeContext.employeeNumber().value(),
                employeeContext.status().getLabel(),
                memo,
                ipAddress
        );
    }

    default EmployeeLogDto.CreateDeleteLogRequest toChangeLogRequest(
            EmployeeContext employeeContext,
            String ipAddress
    ) {
        return new EmployeeLogDto.CreateDeleteLogRequest(employeeContext.employeeNumber().value(), ipAddress);
    }
}
