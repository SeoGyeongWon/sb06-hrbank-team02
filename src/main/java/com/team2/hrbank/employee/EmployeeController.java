package com.team2.hrbank.employee;

import com.team2.hrbank.employee.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "직원 관리", description = "직원 관리 API")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(
            @ModelAttribute CursorPageRequestEmployeeDto request
    ) {
        CursorPageResponseEmployeeDto response = employeeService.getEmployees(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> postEmployees(
            @RequestPart(name = "employee") EmployeeCreateRequest employeeRequest,
            @RequestPart MultipartFile profile,
            HttpServletRequest servletRequest
    ) {
        String ipAddress = servletRequest.getRemoteAddr();
        EmployeeDto response = employeeService.addEmployee(employeeRequest, profile, ipAddress);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto response = employeeService.getEmployee(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        employeeService.deleteEmployee(id, ipAddress);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> patchEmployeeById(
            @PathVariable Long id,
            @RequestPart(name = "employee") EmployeeUpdateRequest employeeRequest,
            @RequestPart MultipartFile profile,
            HttpServletRequest servletRequest
    ) {
        String ipAddress = servletRequest.getRemoteAddr();
        EmployeeDto response = employeeService.updateEmployee(id, employeeRequest, profile, ipAddress);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/trend")
    public ResponseEntity<List<EmployeeTrendDto>> getEmployeeStatsTrend(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam String unit
    ) {
        List<EmployeeTrendDto> response = employeeService.getEmployeeTrends(from, to, unit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/distribution")
    public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeStatsDistribution(
            @RequestParam String groupBy,
            @RequestParam String status
    ) {
        List<EmployeeDistributionDto> response = employeeService.getEmployeeDistributions(groupBy, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeCount(
            @RequestParam String status,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate
    ) {
        Long response = employeeService.getEmployeeCount(status, fromDate, toDate);
        return ResponseEntity.ok(response);
    }
}
