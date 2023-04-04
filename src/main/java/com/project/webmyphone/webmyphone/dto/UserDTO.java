package com.project.webmyphone.webmyphone.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long mataikhoan;
    private String email;
    private String password;
    private String holot;
    private String ten;
    private String sodienthoai;
    private int quyen;

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (quyen == 0) {
            authorities.add(new SimpleGrantedAuthority("Admin"));
        } else if (quyen == 1) {
            authorities.add(new SimpleGrantedAuthority("Staff"));
        } else if (quyen == 2) {
            authorities.add(new SimpleGrantedAuthority("User"));
        }
        return authorities;
    }
}
