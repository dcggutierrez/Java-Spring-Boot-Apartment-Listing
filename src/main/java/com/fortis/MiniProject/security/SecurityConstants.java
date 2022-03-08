package com.fortis.MiniProject.security;

import com.fortis.MiniProject.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10; // ms,sec,min,hour,days = 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
