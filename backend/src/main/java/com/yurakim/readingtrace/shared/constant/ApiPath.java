package com.yurakim.readingtrace.shared.constant;

public final class ApiPath {

    private ApiPath() {}

    public static final String BASE = "/api/v1";
    public static final String BASE_WITH_USERID = BASE + "/users/{userId}";

    public static final String AUTH = BASE + "/auth";
    public static final String ADMIN = BASE + "/admin";
    public static final String USER = BASE + "/users";
    public static final String BOOK = BASE + "/books";

    public static final String USERBOOK = BASE_WITH_USERID + "/books";
    public static final String USERSHELF = BASE_WITH_USERID + "/shelves";
    public static final String USERAI = BASE_WITH_USERID + "/ai";
    public static final String USERNOTE =  BASE_WITH_USERID + "/notes";
    public static final String USERCHAT = BASE_WITH_USERID + "/chats";
    public static final String USERPROFILE =  BASE_WITH_USERID + "/profiles";
    public static final String USERFRIEND = BASE_WITH_USERID + "/friends";

    public static final String GOOGLE_BOOK_BASE = "https://www.googleapis.com/books/v1/volumes";

}
