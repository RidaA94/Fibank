package com.task.fibank.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
    private static final String AUTH_HEADER = "FIB-X-AUTH";
    private static final String API_KEY = "f9Uie8nNf112hx8s"; // Replace with your actual API key

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader(AUTH_HEADER);
        if (API_KEY.equals(apiKey)) {
            return true; // API key is valid, proceed to the controller
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid API Key");
            return false; // Reject the request
        }
    }
}
