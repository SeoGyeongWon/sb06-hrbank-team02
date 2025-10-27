package com.team2.hrbank.changelog.dto;

import java.util.ArrayList;
import java.util.List;

public record DiffDto(
        String propertyName,
        String before,
        String after
) {

    public DiffDto with(String propertyName, String before, String after) {
        return new DiffDto(propertyName, before, after);
    }

    public DiffDto withBefore(String propertyName, String before) {
        return new DiffDto(propertyName, before, null);
    }

    public DiffDto withAfter(String propertyName, String after) {
        return new DiffDto(propertyName, null, after);
    }

    public static class DiffDtoListBuilder {

        private final List<DiffDto> diffList = new ArrayList<>();

        public DiffDtoListBuilder compareAndAdd(String propertyName, String before, String after) {

            if (before == null && after == null) {
                return this;
            }

            if (before == null) {
                diffList.add(new DiffDto(propertyName, null, after));
            } else if (after == null) {
                diffList.add(new DiffDto(propertyName, before, null));
            } else if (!before.equals(after)) {
                diffList.add(new DiffDto(propertyName, before, after));
            }

            return this;

        }

        public List<DiffDto> build() {
            return diffList;
        }

    }


}
