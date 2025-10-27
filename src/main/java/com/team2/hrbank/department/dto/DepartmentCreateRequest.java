package com.team2.hrbank.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DepartmentCreateRequest(

        @NotBlank(message = "부서 이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자 이내여야 합니다.")
        String name,

        @NotBlank(message = "설명은 필수입니다.")
        String description,

        @NotNull(message = "설립일은 필수입니다.")
        LocalDate establishedDate
) {
}
