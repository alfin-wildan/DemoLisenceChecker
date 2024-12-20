package com.example.demo.interceptor;

import com.example.demo.DemoApplication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LicenseCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (DemoApplication.isLicenseExpired()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("License expired. Please renew your license.");
            return false;
        }
        return true;
    }
}
