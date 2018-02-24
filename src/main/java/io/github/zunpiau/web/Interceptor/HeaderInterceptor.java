package io.github.zunpiau.web.Interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderInterceptor extends HandlerInterceptorAdapter {

    private final String headerName;
    private final String headerValue;

    public HeaderInterceptor(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (headerValue.equals(request.getHeader(headerName))) {
            return true;
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
    }
}
