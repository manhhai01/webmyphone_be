package com.project.webmyphone.webmyphone.security;

import com.google.gson.Gson;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exc)
            throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        map.put("success", "false");
        map.put("message", "Tài khoản không có đủ quyền truy cập");
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // response.getWriter().write("Tài khoản không có đủ quyền truy cập");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(jsonString);
    }

}
