package com.team2.hrbank.employee;

import com.team2.hrbank.changelog.dto.EmployeeLogDto;
import com.team2.hrbank.employee.domain.Employee;
import com.team2.hrbank.employee.domain.EmployeeContext;
import com.team2.hrbank.employee.domain.EmployeeStatus;
import com.team2.hrbank.employee.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@NullMarked
@Service
@Transactional(readOnly = true)
public class BasicEmployeeService implements EmployeeService {

    private final EmployeeMapper employeeMapper;
//    private final DepartmentService departmentService;
//    private final ChangeLogService changeLogService;
    private final EmployeeRepository repository;

    public BasicEmployeeService(
            EmployeeMapper employeeMapper,
//            DepartmentService departmentService,
//            ChangeLogService changeLogService,
            EmployeeRepository employeeRepository
    ) {
        this.employeeMapper = employeeMapper;
//        this.departmentService = departmentService;
//        this.changeLogService = changeLogService;
        this.repository = employeeRepository;
    }

    @Override
    public CursorPageResponseEmployeeDto getEmployees(CursorPageRequestEmployeeDto request) {
        return repository.searchAllBy(request);
    }

    @Override
    @Transactional
    public EmployeeDto addEmployee(EmployeeCreateRequest request, MultipartFile multipartFile, String ipAddress) {
        EmployeeContext employeeContext = employeeMapper.toEntityContext(request);

        Employee employee = repository.save(Employee.from(employeeContext));
        String departmentName = null; /* departmentService.getDepartmentName(employee.getDepartmentId()) */ // TODO: 부서명 가져오기
        Long profileImageId = saveProfileIfPresent(multipartFile, null /* fileService::save */); // TODO: 파일 관리 서비스를 통해 저장

        EmployeeLogDto.CreateLogRequest changeLogRequest =
                createChangeLogRequest(employee, departmentName, request.memo(), ipAddress);
//        saveChangeLog(changeLogRequest, changeLogService::addEmployeeInsertLog);

        return employeeMapper.toDto(employee, departmentName, profileImageId);
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        String departmentName = null; /* departmentService.getDepartmentName(employee.getDepartmentId()) */ // TODO: 부서명 가져오기
        Long profileImageId = null; /* fileService.getProfileImageId(employee.getId()) */ // TODO: 파일 관리 서비스를 통해 ID 가져오기

        return employeeMapper.toDto(employee, departmentName, profileImageId);
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteEmployee(Long id, String ipAddress) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        /* fileService.deleteProfileImage(employee.getId()); */ // TODO: 이미지 파일 삭제
        repository.delete(employee);
        String departmentName = null; /* departmentService.getDepartmentName(employee.getDepartmentId()) */ // TODO: 부서명 가져오기

        EmployeeLogDto.CreateDeleteLogRequest changeLogRequest = createChangeLogRequest(employee, ipAddress);
//        saveChangeLog(changeLogRequest, changeLogService::addEmployeeDeleteLog);
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public EmployeeDto updateEmployee(
            Long id,
            EmployeeUpdateRequest request,
            MultipartFile multipartFile,
            String ipAddress
    ) {
        EmployeeContext employeeContext = employeeMapper.toEntityContext(request);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id))
                .load(employeeContext);
        String departmentName = null; /* departmentService.getDepartmentName(employee.getDepartmentId()) */ // TODO: 부서명 가져오기
        Long profileImageId = saveProfileIfPresent(multipartFile, profile -> null /* fileService::save */); // TODO: 파일 관리 서비스를 통해 저장

        EmployeeLogDto.CreateLogRequest changeLogRequest =
                createChangeLogRequest(employee, departmentName, request.memo(), ipAddress);
//        saveChangeLog(changeLogRequest, changeLogService::addEmployeeUpdateLog);

        return employeeMapper.toDto(employee, departmentName, profileImageId);
    }

    @Override
    public List<EmployeeTrendDto> getEmployeeTrends(LocalDate from, LocalDate to, String unit) {
        return repository.findTrendsBy(from, to, unit);
    }

    @Override
    public List<EmployeeDistributionDto> getEmployeeDistributions(String groupBy, String employeeStatus) {
        EmployeeStatus status = EmployeeStatus.valueOf(employeeStatus.toUpperCase());
        return repository.findDistributionsBy(groupBy, status);
    }

    @Override
    public Long getEmployeeCount(String employeeStatus, LocalDate fromDate, LocalDate toDate) {
        EmployeeStatus status = EmployeeStatus.valueOf(employeeStatus.toUpperCase());
        return repository.countByStatusAndHireDateBetween(status, fromDate, toDate);
    }

    @Override
    public BackupEmployeePageResponseDto getEmployees(BackupEmployeePageRequestDto employee) {
        return null;
    }

    @Override
    public List<DepartmentEmployeeCountDto> findEmployeeCountsByDepartmentId() {
        return null;
    }

    @Nullable
    private static Long saveProfileIfPresent(MultipartFile multipartFile, Function<MultipartFile, Long> saver) {
        if (!multipartFile.isEmpty()) {
            return saver.apply(multipartFile);
        }
        return null;
    }

    private EmployeeLogDto.CreateLogRequest createChangeLogRequest(
            Employee employee,
            String departmentName,
            String memo,
            String ipAddress
    ) {
        return employeeMapper.toChangeLogRequest(employee.save(), departmentName, memo, ipAddress);
    }

    private EmployeeLogDto.CreateDeleteLogRequest createChangeLogRequest(
            Employee employee,
            String ipAddress
    ) {
        return employeeMapper.toChangeLogRequest(employee.save(), ipAddress);
    }

    private static <T> void saveChangeLog(T request, Consumer<T> saver) {
        saver.accept(request);
    }
}
