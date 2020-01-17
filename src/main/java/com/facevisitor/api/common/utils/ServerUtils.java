package com.facevisitor.api.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class ServerUtils {

    public static String myHostname(HttpServletRequest request) {
        String scheme = StringUtils.isNotEmpty(request.getHeader("x-forwarded-proto")) ? request.getHeader("x-forwarded-proto") : request.getScheme();
        final String host = scheme + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort()));
        return host;
    }
}
