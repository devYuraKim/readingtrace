package com.yurakim.readingtrace.auth.constant;

import java.time.Duration;

public final class JWT {

    private JWT() {}

    public static final String JWT_HEADER= "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // Property keys for Environment lookup
    public static final String JWT_SECRET_KEY_PROPERTY = "jwt.secret-key";
    public static final String JWT_ISSUER_PROPERTY = "jwt.issuer";
    public static final String JWT_EXPIRATION_PROPERTY = "jwt.expiration-hours";

    // Default fallback values (should only be used in development/testing)
    public static final String JWT_SECRET_KEY_DEFAULT_VALUE = "iJ9xSwVWOLmMjsUIdcHRCJCxcjjE2A7N";
    public static final String JWT_ISSUER_DEFAULT = "readingtrace_dev";
    public static final Integer JWT_EXPIRATION_HOURS_DEFAULT = 12;

    // Computed default values
    public static final Long JWT_EXPIRATION_DEFAULT = Duration.ofHours(JWT_EXPIRATION_HOURS_DEFAULT).toMillis();

}
