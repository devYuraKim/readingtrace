package com.yurakim.readingtrace.auth.constant;

public final class JWT {

    private JWT() {}

    public static final String JWT_HEADER= "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH-TOKEN";
    public static final String JWT_PREFIX = "Bearer ";

    public static final String JWT_SECRET_KEY_PROPERTY = "jwt.secret";
    public static final String JWT_ISSUER_PROPERTY = "jwt.issuer";

    public static final String JWT_EXPIRATION_PROPERTY = "jwt.expiration-hours";

    public static final String ACCESS_SECRET_KEY_PROPERTY = "jwt.access-secret";
    public static final String REFRESH_SECRET_KEY_PROPERTY = "jwt.refresh-secret";
    public static final String ACCESS_EXPIRATION_MINUTES_PROPERTY = "jwt.access-expiration-minutes";
    public static final String REFRESH_EXPIRATION_DAYS_PROPERTY = "jwt.refresh-expiration-days";

    public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";
    public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";
    public static final String REVOKED_REFRESH_TOKEN = "revoked_refresh_token";

}