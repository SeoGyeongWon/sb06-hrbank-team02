package com.team2.hrbank.changelog.domain;

public enum ChangeLogType {

    CREATED("생성"),
    UPDATED("수정"),
    DELETED("삭제");

    private String description;

    ChangeLogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
