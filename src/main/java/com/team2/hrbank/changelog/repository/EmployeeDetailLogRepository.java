package com.team2.hrbank.changelog.repository;

import com.team2.hrbank.changelog.domain.EmployeeDetailLog;
import com.team2.hrbank.changelog.domain.QEmployeeDetailLog;
import io.github.openfeign.querydsl.jpa.spring.repository.QuerydslJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDetailLogRepository extends QuerydslJpaRepository<EmployeeDetailLog, Long> {

    QEmployeeDetailLog employeeDetailLog = QEmployeeDetailLog.employeeDetailLog;

    default List<EmployeeDetailLog> findByChangeLogId(Long changeLogId) {
        return selectFrom(employeeDetailLog)
                .where(employeeDetailLog.changeLog.id.eq(changeLogId))
                .fetch();
    }

    default Optional<EmployeeDetailLog> findByEmployeeNumber(String employeeNumber) {
        return Optional.ofNullable((selectFrom(employeeDetailLog)
                .where(employeeDetailLog.changeLog.employeeNumber.eq(employeeNumber))
                .fetchOne()));
    }

}
