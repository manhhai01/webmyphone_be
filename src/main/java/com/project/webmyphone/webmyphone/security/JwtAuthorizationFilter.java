package com.project.webmyphone.webmyphone.security;

import com.project.webmyphone.webmyphone.dto.UserDTO;
import com.project.webmyphone.webmyphone.service.JwtService;
import com.project.webmyphone.webmyphone.service.UserService;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthorizationFilter extends
        UsernamePasswordAuthenticationFilter {
    private final static String Token_header = "Authorization";

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtService jwtService, UserService userService) {
        super(authManager);
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(Token_header);
        if (jwtService.validateTokenLogin(authToken)) {
            String email = jwtService.getEmailFromToken(authToken);
            UserDTO uDTO = userService.loadUserDTOByEmail(email);
            if (uDTO != null) {
                boolean enabled = true;
                boolean accountNonExpired = true;
                boolean credentialsNonExpired = true;
                boolean accountNonLocked = true;
                UserDetails userDetail = new User(email, uDTO.getPassword(), enabled,
                        accountNonExpired,
                        credentialsNonExpired, accountNonLocked, uDTO.getAuthorities());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
                        null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
