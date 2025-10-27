package com.team2.hrbank.changelog.service;

import com.team2.hrbank.changelog.domain.*;
import com.team2.hrbank.changelog.dto.*;
import com.team2.hrbank.changelog.mapper.ChangeLogMapper;
import com.team2.hrbank.changelog.repository.ChangeLogRepository;
import com.team2.hrbank.changelog.repository.EmployeeDetailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicChangeLogService implements ChangeLogService{

    private final ChangeLogRepository changeLogRepository;
    private final EmployeeDetailLogRepository employeeDetailLogRepository;
    private final ChangeLogMapper changeLogMapper;
    
    @Override
    public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogRequestDto.PaginatedLogRequest request) {
        // return paginated logs from repository
        Slice<ChangeLog> changeLogSlice = changeLogRepository.findChangeLogs(request);
        List<ChangeLogDto> changeLogDtoList = changeLogMapper.toDtoList(changeLogSlice.getContent());
        changeLogDtoList.remove(changeLogDtoList.size()-1);

        return CursorPageResponseChangeLogDto.builder()
                .content(changeLogDtoList)
                .nextCursor(changeLogSlice.hasNext() ?
                        changeLogSlice.getContent().get(changeLogSlice.getContent().size() -1).getAt().toString() : null)
                .nextIdAfter(changeLogSlice.hasNext() ?
                        changeLogSlice.getContent().get(changeLogSlice.getContent().size() -1).getId() : null)
                .size(request.size())
                .totalElements(changeLogRepository.countChangeLogs())
                .hasNext(changeLogSlice.hasNext()).build();
    }

    @Override
    public List<DiffDto> getDiffByChangeLogId(Long id) {

        // get ChangeLog and List of EmployeeLogDiff by ChangeLog id from repository
        List<EmployeeDetailLog> employeeDetailLogList = employeeDetailLogRepository.findByChangeLogId(id);

        // if size is 1
        if (employeeDetailLogList.size() == 1) {
            EmployeeDetailLog log = employeeDetailLogList.get(0);
            ChangeLog changeLog = log.getChangeLog();
            if (changeLog.getType() == ChangeLogType.DELETED) {
                return getDiffAllFieldsAsBefore(log);
            } else if (changeLog.getType() == ChangeLogType.CREATED) {
                return getDiffAllFieldsAsAfter(log, changeLog.getEmployeeNumber());
            }
        }

        employeeDetailLogList.sort((o1, o2) -> {
            if (o1.getType() == EmployeeLogType.BEFORE && o2.getType() == EmployeeLogType.AFTER) {
                return -1;
            } else {
                return 1;
            }
        });

        // if size is 2
        return getDiff(employeeDetailLogList.get(1), employeeDetailLogList.get(0));

    }

    private List<DiffDto> getDiffAllFieldsAsBefore(EmployeeDetailLog log) {
        return new DiffDto.DiffDtoListBuilder()
                .compareAndAdd("입사일", log.getHireDate().toString(), null)
                .compareAndAdd("이름", log.getName(), null)
                .compareAndAdd("직함", log.getPosition(), null)
                .compareAndAdd("부서명", log.getDepartment(), null)
                .compareAndAdd("이메일", log.getEmail().email(), null)
                .compareAndAdd("상태", log.getStatus(), null)
                .build();
    }

    private List<DiffDto> getDiffAllFieldsAsAfter(EmployeeDetailLog log, String employeeNumber) {
        return new DiffDto.DiffDtoListBuilder()
                .compareAndAdd("입사일", null, log.getHireDate().toString())
                .compareAndAdd("이름", null, log.getName())
                .compareAndAdd("직함", null, log.getPosition())
                .compareAndAdd("부서명", null, log.getDepartment())
                .compareAndAdd("이메일", null, log.getEmail().email())
                .compareAndAdd("사번", null, employeeNumber)
                .compareAndAdd("상태", null, log.getStatus())
                .build();
    }

    private List<DiffDto> getDiff(EmployeeDetailLog beforeLog, EmployeeDetailLog afterLog) {
        return new DiffDto.DiffDtoListBuilder()
                .compareAndAdd("입사일", beforeLog.getHireDate().toString(),  afterLog.getHireDate().toString())
                .compareAndAdd("이름", beforeLog.getName(),  afterLog.getName())
                .compareAndAdd("직함", beforeLog.getPosition(),  afterLog.getPosition())
                .compareAndAdd("부서명", beforeLog.getDepartment(),  afterLog.getDepartment())
                .compareAndAdd("이메일", beforeLog.getEmail().email(),  afterLog.getEmail().email())
                .compareAndAdd("상태", beforeLog.getStatus(),  afterLog.getStatus())
                .build();
    }

    @Override
    public Long getTotalCount(LocalDateTime fromDate, LocalDateTime toDate) {
        // return count from repository
        return changeLogRepository.countChangeLogsByDate(fromDate, toDate);
    }

    @Override
    public LocalDateTime getRecentLogDate() {
        // return recent log date from repository
        return changeLogRepository.findRecentLogDate().orElse(null);
    }

    @Transactional
    @Override
    public ChangeLogDto addEmployeeInsertLog(EmployeeLogDto.CreateLogRequest request) {

        // insert ChangeLog and return entity
        ChangeLog changeLog = changeLogRepository.save(ChangeLog.builder()
                .type(ChangeLogType.CREATED)
                .employeeNumber(request.employeeNumber())
                .memo(request.memo())
                .ipAddress(new IPAddress(request.ipAddress()))
                .build());

        // insert EmployeeLog as after log
        employeeDetailLogRepository.save(EmployeeDetailLog.builder()
                .changeLog(changeLog)
                .type(EmployeeLogType.AFTER)
                .hireDate(request.hireDate())
                .name(request.employeeName())
                .position(request.employeePosition())
                .department(request.departmentName())
                .email(new EmployeeEmail(request.employeeEmail()))
                .status(request.employeeStatus())
                .build());

        // convert to ChangeLogDto and return
        return changeLogMapper.toDto(changeLog);

    }

    @Transactional
    @Override
    public ChangeLogDto addEmployeeUpdateLog(EmployeeLogDto.CreateLogRequest request) {

        // get after Employee log of same employeeNumber
        EmployeeDetailLog beforeLog = employeeDetailLogRepository.findByEmployeeNumber(request.employeeNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 사원의 로그를 찾을 수 없습니다: " + request.employeeNumber()));

        // insert ChangeLog and return entity
        ChangeLog changeLog = changeLogRepository.save(ChangeLog.builder()
                .type(ChangeLogType.UPDATED)
                .employeeNumber(request.employeeNumber())
                .memo(request.memo())
                .ipAddress(new IPAddress(request.ipAddress()))
                .build());

        // insert EmployeeLog
        beforeLog = EmployeeDetailLog.builder()
                .changeLog(changeLog)
                .type(EmployeeLogType.BEFORE)
                .hireDate(beforeLog.getHireDate())
                .name(beforeLog.getName())
                .position(beforeLog.getPosition())
                .department(beforeLog.getDepartment())
                .email(beforeLog.getEmail())
                .status(beforeLog.getStatus())
                .build();

        EmployeeDetailLog afterLog = EmployeeDetailLog.builder()
                .changeLog(changeLog)
                .type(EmployeeLogType.AFTER)
                .hireDate(request.hireDate())
                .name(request.employeeName())
                .position(request.employeePosition())
                .department(request.departmentName())
                .email(new EmployeeEmail(request.employeeEmail()))
                .status(request.employeeStatus())
                .build();

        employeeDetailLogRepository.saveAll(List.of(beforeLog, afterLog));
        // convert to ChangeLogDto and return
        return changeLogMapper.toDto(changeLog);

    }

    @Transactional
    @Override
    public ChangeLogDto addEmployeeDeleteLog(EmployeeLogDto.CreateDeleteLogRequest request) {

        // get after Employee log of same employeeNumber
        EmployeeDetailLog beforeLog = employeeDetailLogRepository.findByEmployeeNumber(request.employeeNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 사원의 로그를 찾을 수 없습니다: " + request.employeeNumber()));

        // insert ChangeLog as before and return entity
        ChangeLog changeLog = changeLogRepository.save(ChangeLog.builder()
                .type(ChangeLogType.DELETED)
                .employeeNumber(request.employeeNumber())
                .ipAddress(new IPAddress(request.ipAddress()))
                .build());

        // insert EmployeeLog
        employeeDetailLogRepository.save(EmployeeDetailLog.builder()
                .changeLog(changeLog)
                .type(EmployeeLogType.BEFORE)
                .hireDate(beforeLog.getHireDate())
                .name(beforeLog.getName())
                .position(beforeLog.getPosition())
                .department(beforeLog.getDepartment())
                .email(beforeLog.getEmail())
                .status(beforeLog.getStatus())
                .build());

        // convert to ChangeLogDto and return
        return changeLogMapper.toDto(changeLog);

    }


}
