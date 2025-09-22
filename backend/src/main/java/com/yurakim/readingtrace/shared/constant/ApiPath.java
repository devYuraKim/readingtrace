package com.yurakim.readingtrace.shared.constant;

public final class ApiPath {

    private ApiPath() {}

    public static final String BASE = "/api/v1";

    public static final String USER = BASE + "/user";
    public static final String AUTH = BASE + "/auth";
    public static final String ADMIN = BASE + "/admin";
    public static final String BOOK = BASE + "/book";

    public static final String GOOGLE_BOOK_BASE = "https://www.googleapis.com/books/v1/volumes";

}
