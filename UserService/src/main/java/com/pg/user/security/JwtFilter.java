package com.pg.user.security;

import com.pg.user.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    AuthenticationManager authenticationManager;
    JwtUtil jwtutil;

    @Autowired
    public JwtFilter(AuthenticationManager authenticationManager,JwtUtil jwtutil){
        this.authenticationManager=authenticationManager;
        this.jwtutil=jwtutil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/generate-token")){
            filterChain.doFilter(request,response);
            return;
        }
        ObjectMapper objectMapper=new ObjectMapper();
        LoginRequest loginRequest=objectMapper.readValue(request.getInputStream(),LoginRequest.class);
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());
        Authentication authentication=authenticationManager.authenticate(authToken);
        if(authentication.isAuthenticated()){
            String token=jwtutil.generateToken(loginRequest.getUsername(),60);
            response.setHeader("Authorization","Bearer "+token);
        }
    }
}
