package com.ecommerce.app.filters;

import com.ecommerce.app.security.JwtUtilService;
import com.ecommerce.app.service.UserService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = jwtUtilService.getToken(request);

        if (token != null && jwtUtilService.validateToken(token))
        {
            String email = jwtUtilService.extractUsername(token);

            UserDetails userDetails = userService.loadUserByUsername(email);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        chain.doFilter(request,response);

    }

}