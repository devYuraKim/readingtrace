package com.yurakim.readingtrace.auth.constant;

public final class JWT {

    private JWT() {}

    public static final String JWT_HEADER= "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    public static final String JWT_SECRET_KEY_PROPERTY = "jwt.secret";
    public static final String JWT_ISSUER_PROPERTY = "jwt.issuer";
    public static final String JWT_EXPIRATION_PROPERTY = "jwt.expiration-hours";

}
