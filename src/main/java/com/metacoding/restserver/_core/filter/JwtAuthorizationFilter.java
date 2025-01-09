package com.metacoding.restserver._core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver._core.util.JwtUtil;
import com.metacoding.restserver._core.util.Resp;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {
    // ObjectMapper objectMapper = new ObjectMapper();

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("debug: JWT Authorization Filter 작동됨.............");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String jwt = request.getHeader("Authorization");

        if (jwt == null){
            onError(response,"토큰없음..");
            return;
        }

        if (!jwt.startsWith("Bearer ")){
            onError(response,"프로토콜 잘못됨");
            return;
        }

        LoginUser loginUser = jwtUtil.verify(jwt);

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("sessionUser", loginUser);
        chain.doFilter(request,response);
    }

    private void onError(HttpServletResponse response, String msg){
        try {
            String responseBody = new ObjectMapper().writeValueAsString(Resp.fail(msg));
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(responseBody);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
