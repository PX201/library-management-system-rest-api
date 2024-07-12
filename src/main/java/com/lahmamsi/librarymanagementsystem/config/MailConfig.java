package com.lahmamsi.librarymanagementsystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MailConfig {


    @Value("${mail.password}")
    private String password;

    @Value("${mail.username}")
    private String username;
    
    @Value("${mail.port}")
    private String port;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.auth}")
    private boolean auth;

    @Value("${mail.starttls.enable}")
    private boolean tlsEnable;

    @Value("${mail.ssl.trust}")
    private String sslTrust;

    private static String staticUsername;
    private static String staticPassword;
    private static String staticPort;
    private static String staticHost;
    private static boolean staticAuth;
    private static boolean staticTlsEnable;
    private static String staticSslTrust;

    @PostConstruct
    public void init() {
        staticUsername = this.username;
        staticPassword = this.password;
        staticPort = this.port;
        staticHost = this.host;
        staticAuth = this.auth;
        staticTlsEnable = this.tlsEnable;
        staticSslTrust = this.sslTrust;
    }

    public static String getUsername() {
        return staticUsername;
    }

    public static String getPassword() {
        return staticPassword;
    }

    public static String getPort() {
        return staticPort;
    }

    public static String getHost() {
        return staticHost;
    }

    public static boolean isAuth() {
        return staticAuth;
    }

    public static boolean isTlsEnable() {
        return staticTlsEnable;
    }

    public static String getSslTrust() {
        return staticSslTrust;
    }
}
