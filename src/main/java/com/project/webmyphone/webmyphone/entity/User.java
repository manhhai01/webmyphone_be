package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Taikhoan")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTaiKhoan")
    private long mataikhoan;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "HoLot", nullable = false)
    private String holot;

    @Column(name = "Ten", nullable = false)
    private String ten;

    @Column(name = "SoDienThoai")
    private String sodienthoai;

    @Column(name = "Quyen")
    private int quyen;

    @Column(name = "TrangThai")
    private int trangthai;
}
