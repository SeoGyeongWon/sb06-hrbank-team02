package com.team2.hrbank.department.dto;

import java.util.List;

public record CursorPageResponseDepartmentDto(
        List<DepartmentDto> content,  //현제 페이지 부서목록
        boolean hasNext,  //다음 페이지 존재여부
        Long nextIdAfter,  //다음 페이지 조회시 사용될 id
        String nextCursor,  //명세에 있어서 추가
        int size,
        Long totalElements  //커서기반 이지만, 명세에 있어서 추가
) {
    // 조회 결과가 비어있거나, 다음 페이지가 없는 경우를 위한 정적 팩토리 메서드
    public static CursorPageResponseDepartmentDto empty() {
        return new CursorPageResponseDepartmentDto(List.of(), false, null,
                null, 0, 0L);
    }
}
