package com.project.webmyphone.webmyphone.config;

import com.project.webmyphone.webmyphone.security.CustomAccessDeniedHandler;
import com.project.webmyphone.webmyphone.security.JwtAuthorizationFilter;
import com.project.webmyphone.webmyphone.security.JwtService;
import com.project.webmyphone.webmyphone.security.RestAuthenticationEntryPoint;
import com.project.webmyphone.webmyphone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**");
        http.authorizeRequests().antMatchers("/api/v1/user/login", "/api/v1/user/register").permitAll();
        http.antMatcher("/**").httpBasic()
                .authenticationEntryPoint(restServicesEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                // User
                .antMatchers(HttpMethod.GET, "/api/v1/user/currentdetail")
                .access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.GET, "/api/v1/user").access("hasAuthority('Admin')")
                .antMatchers(HttpMethod.PUT, "/api/v1/user/password").access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.POST, "/api/v1/user/add").access("hasAuthority('Admin')")
                .antMatchers(HttpMethod.PUT, "/api/v1/user").access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/user/**").access("hasAuthority('Admin')")

                // Statistical
                .antMatchers(HttpMethod.GET, "/api/v1/statistical").access("hasAuthority('Admin')")
                .antMatchers(HttpMethod.POST, "/api/v1/statistical").access("hasAuthority('Admin')")

                // Order
                .antMatchers(HttpMethod.POST, "/api/v1/order").access("hasAuthority('User')")
                .antMatchers(HttpMethod.PUT, "/api/v1/order/**").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/order/**").access("hasAnyAuthority('Admin','User')")
                .antMatchers(HttpMethod.GET, "/api/v1/order/**").access("hasAnyAuthority('Admin','Staff','User')")

                // Cart
                .antMatchers(HttpMethod.POST, "/api/v1/cart/**").access("hasAuthority('User')")
                .antMatchers(HttpMethod.GET, "/api/v1/cart").access("hasAuthority('User')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/cart/**").access("hasAuthority('User')")
                .antMatchers(HttpMethod.PUT, "/api/v1/cart").access("hasAuthority('User')")

                // ImportGoods
                .antMatchers(HttpMethod.GET, "/api/v1/import/**").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.GET, "/api/v1/import").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.POST, "/api/v1/import").access("hasAnyAuthority('Admin','Staff')")

                // Product
                .antMatchers(HttpMethod.GET, "/api/v1/product/**").access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.POST, "/api/v1/product").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/product/**").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.PUT, "/api/v1/product/**").access("hasAnyAuthority('Admin','Staff')")

                // Category
                .antMatchers(HttpMethod.GET, "/api/v1/category/**").access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.POST, "/api/v1/category").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/category/**").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.PUT, "/api/v1/category/**").access("hasAnyAuthority('Admin','Staff')")

                // Trademark
                .antMatchers(HttpMethod.GET, "/api/v1/trademark/**").access("hasAnyAuthority('Admin','Staff','User')")
                .antMatchers(HttpMethod.POST, "/api/v1/trademark").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/trademark/**").access("hasAnyAuthority('Admin','Staff')")
                .antMatchers(HttpMethod.PUT, "/api/v1/trademark/**").access("hasAnyAuthority('Admin','Staff')")
                .and()
                .addFilter(new JwtAuthorizationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                        jwtService, userService))
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
        return http.build();
    }
}
