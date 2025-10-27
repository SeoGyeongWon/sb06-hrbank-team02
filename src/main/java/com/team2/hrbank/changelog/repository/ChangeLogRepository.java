package com.team2.hrbank.changelog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.team2.hrbank.changelog.domain.ChangeLog;
import com.team2.hrbank.changelog.domain.QChangeLog;
import com.team2.hrbank.changelog.dto.ChangeLogRequestDto;
import io.github.openfeign.querydsl.jpa.spring.repository.QuerydslJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeLogRepository extends QuerydslJpaRepository<ChangeLog, Long> {

    QChangeLog changeLog = QChangeLog.changeLog;

    default Slice<ChangeLog> findChangeLogs(ChangeLogRequestDto.PaginatedLogRequest request) {
        // Implementation placeholder
        BooleanBuilder builder = buildPredicate(request);
        OrderSpecifier<?> orderSpecifier = buildDynamicOrderSpecifier(request.sortField(), request.sortDirection());

        List<ChangeLog> changeLogList = selectFrom(changeLog)
                .where(builder)
                .orderBy(orderSpecifier)
                .limit(request.size() + 1)
                .fetch();

        boolean hasNext = changeLogList.size() > request.size();

        return new SliceImpl<>(changeLogList, Pageable.ofSize(request.size()), hasNext);
    }

    private BooleanBuilder buildPredicate(ChangeLogRequestDto.PaginatedLogRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.cursor() != null) {
            LocalDateTime cursorDateTime = LocalDateTime.parse(request.cursor());
            if (request.sortDirection().equalsIgnoreCase("ASC")) {
                builder.and(changeLog.at.gt(cursorDateTime));
            } else {
                builder.and(changeLog.at.lt(cursorDateTime));
            }
        }

        if (request.employeeNumber() != null) {
            builder.and(changeLog.employeeNumber.eq(request.employeeNumber()));
        }
        if (request.type() != null) {
            builder.and(changeLog.type.eq(request.type()));
        }
        if (request.memo() != null) {
            builder.and(changeLog.memo.containsIgnoreCase(request.memo()));
        }
        if (request.atFrom() != null && request.atTo() != null) {
            builder.and(changeLog.at.between(request.atFrom(), request.atTo()));
        } else if (request.atFrom() != null) {
            builder.and(changeLog.at.goe(request.atFrom()));
        } else if (request.atTo() != null) {
            builder.and(changeLog.at.loe(request.atTo()));
        }

        return builder;
    }

    private OrderSpecifier<?> buildDynamicOrderSpecifier(String sortField, String direction) {

        Order order = direction.equalsIgnoreCase("ASC") ? Order.ASC : Order.DESC;

        PathBuilder<QChangeLog> pathBuilder = new PathBuilder<>(QChangeLog.class, QChangeLog.changeLog.getMetadata());

        return new OrderSpecifier<>(order, pathBuilder.getString(sortField));

    }

    default Long countChangeLogs() {
        return selectFrom(changeLog)
                .fetchCount();
    }

    default Long countChangeLogsByDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return selectFrom(changeLog)
                .where(changeLog.at.between(fromDate, toDate))
                .fetchCount();
    }

    default Optional<LocalDateTime> findRecentLogDate() {
        return select(changeLog.at)
                .from(changeLog)
                .orderBy(changeLog.at.desc())
                .limit(1)
                .fetch()
                .stream()
                .findFirst();
    }

}
