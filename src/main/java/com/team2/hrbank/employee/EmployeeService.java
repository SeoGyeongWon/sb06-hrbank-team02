package com.team2.hrbank.employee;

import com.team2.hrbank.employee.dto.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@NullMarked
public interface EmployeeService {

    CursorPageResponseEmployeeDto getEmployees(CursorPageRequestEmployeeDto employee);

    EmployeeDto addEmployee(EmployeeCreateRequest employee, MultipartFile profile, String ipAddress);

    EmployeeDto getEmployee(Long id);

    void deleteEmployee(Long id, String ipAddress);

    EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest employee, MultipartFile profile, String ipAddress);

    List<EmployeeTrendDto> getEmployeeTrends(LocalDate from, LocalDate to, String unit);

    List<EmployeeDistributionDto> getEmployeeDistributions(String groupBy, String employeeStatus);

    Long getEmployeeCount(String employeeStatus, LocalDate fromDate, LocalDate toDate);

    BackupEmployeePageResponseDto getEmployees(BackupEmployeePageRequestDto employee);

    List<DepartmentEmployeeCountDto> findEmployeeCountsByDepartmentId();
}
