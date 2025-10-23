package com.yurakim.readingtrace.shelf.enums;

import lombok.Getter;

@Getter
public enum DefaultShelfType {

    //TODO: AVOID MAGIC STRING, set the name and the derive slug
    ALREADY_READ("Already Read", "already-read", 1),
    WANT_TO_READ("Want To Read", "want-to-read", 2),
    CURRENTLY_READING("Currently Reading", "currently-reading", 3),
    PAUSED_READING("Paused Reading", "paused-reading", 4),
    NEVER_FINISHED("Never Finished", "never-finished", 5);

    private final String name;
    private final String slug;
    private final Integer orderIndex;

    DefaultShelfType(String name, String slug, Integer orderIndex) {
        this.name = name;
        this.slug = slug;
        this.orderIndex = orderIndex;
    }

}