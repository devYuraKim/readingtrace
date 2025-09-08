package com.yurakim.readingtrace.auth.constant;

import java.time.Duration;

public final class JWT {

    private JWT() {}

    public static final String JWT_SECRET_KEY = "JWT_SECRET_KEY";
    public static final String JWT_SECRET_KEY_DEFAULT_VALUE = "iJ9xSwVWOLmMjsUIdcHRCJCxcjjE2A7N";
    public static final String JWT_ISSUER = "readingtrace";
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final Long JWT_EXPIRATION = Duration.ofHours(12).toMillis();

}
