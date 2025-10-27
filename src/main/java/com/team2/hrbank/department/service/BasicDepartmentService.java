package com.team2.hrbank.department.service;

import com.team2.hrbank.department.domain.Department;
import com.team2.hrbank.department.dto.CursorPageResponseDepartmentDto;
import com.team2.hrbank.department.dto.DepartmentCreateRequest;
import com.team2.hrbank.department.dto.DepartmentDto;
import com.team2.hrbank.department.dto.DepartmentMapper;
import com.team2.hrbank.department.dto.DepartmentUpdateRequest;
import com.team2.hrbank.department.repository.DepartmentRepository;
import com.team2.hrbank.employee.EmployeeService;
import com.team2.hrbank.employee.dto.DepartmentEmployeeCountDto;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;
  private final EmployeeService employeeService;

  private DepartmentDto toDtoWithCount(Department department) {
    DepartmentDto dto = departmentMapper.toDto(department);
    Long employeeCount = getEmployeeCount(department.getId());
    return new DepartmentDto(
        dto.id(),
        dto.name(),
        dto.description(),
        dto.establishedDate(),
        employeeCount
    );
  }

  private long getEmployeeCount(Long departmentId) {
    List<DepartmentEmployeeCountDto> counts = employeeService.findEmployeeCountsByDepartmentId();

    return counts.stream()
        .filter(c -> c.departmentId().equals(departmentId))
        .findFirst()
        .map(DepartmentEmployeeCountDto::employeeCount)
        .orElse(0L);
  }

  private List<DepartmentDto> toDtoListWithCounts(List<Department> departments) {
    if (departments.isEmpty()) {
      return List.of();
    }

    List<DepartmentEmployeeCountDto> counts = employeeService.findEmployeeCountsByDepartmentId();

    Map<Long, Long> countMap = counts.stream()
        .collect(Collectors.toMap(
            DepartmentEmployeeCountDto::departmentId,
            DepartmentEmployeeCountDto::employeeCount
        ));

    return departments.stream()
        .map(department -> {
          DepartmentDto dto = departmentMapper.toDto(department);
          Long employeeCount = countMap.getOrDefault(department.getId(), 0L);
          return new DepartmentDto(
              dto.id(),
              dto.name(),
              dto.description(),
              dto.establishedDate(),
              employeeCount
          );
        })
        .collect(Collectors.toList());
  }
  @Override
  public DepartmentDto createDepartment(DepartmentCreateRequest request) {
    if (departmentRepository.existsByName(request.name())) {
      throw new ResponseStatusException(CONFLICT, "이미 존재하는 부서 이름입니다: " + request.name());
    }
    Department department = departmentMapper.toEntity(request);
    Department savedDepartment = departmentRepository.save(department);
    return toDtoWithCount(savedDepartment);
  }

  @Override
  public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "부서를 찾을 수 없습니다. ID: " + id));

    if (!department.getName().equals(request.name()) && departmentRepository.existsByName(request.name())) {
      throw new ResponseStatusException(CONFLICT, "이미 존재하는 부서 이름으로 수정할 수 없습니다: " + request.name());
    }

    department.update(request);
    return toDtoWithCount(department);
  }

  @Override
  public void deleteDepartment(Long id) {
    if (!departmentRepository.existsById(id)) {
      throw new ResponseStatusException(NOT_FOUND, "부서를 찾을 수 없습니다. ID: " + id);
    }

    if (getEmployeeCount(id) > 0) {
      throw new ResponseStatusException(BAD_REQUEST, "소속 직원이 있는 부서는 삭제할 수 없습니다. ID: " + id);
    }

    departmentRepository.deleteById(id);
  }

  @Override
  public DepartmentDto getDepartment(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "부서를 찾을 수 없습니다. ID: " + id));

    return toDtoWithCount(department);
  }

  @Override
  public CursorPageResponseDepartmentDto getDepartments(
      String nameOrDescription,
      Long idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection
  ) {
    String finalSortField = (sortField == null || sortField.isBlank()) ? "establishedDate" : sortField;
    if (!("name".equalsIgnoreCase(finalSortField) || "establishedDate".equalsIgnoreCase(finalSortField))) {
      throw new ResponseStatusException(BAD_REQUEST, "유효하지 않은 정렬 기준입니다: " + sortField);
    }

    List<Department> entities = departmentRepository.findDepartmentsWithCursor(
        nameOrDescription,
        finalSortField,
        sortDirection,
        idAfter,
        size + 1
    );

    // 커서 기반 페이지네이션 메타데이터 계산
    boolean hasNext = entities.size() > size;
    List<Department> departments = hasNext ? entities.subList(0, size) : entities;
    Long nextId = hasNext ? departments.get(departments.size() - 1).getId() : null;

    List<DepartmentDto> departmentDtos = toDtoListWithCounts(departments);

    return new CursorPageResponseDepartmentDto(
        departmentDtos,
        hasNext,
        nextId,
        null,   // nextCursor (복합 정렬이 없으므로 null)
        size,
        0L      // totalElements (커서 기반이므로 0L 유지)
    );
  }
}