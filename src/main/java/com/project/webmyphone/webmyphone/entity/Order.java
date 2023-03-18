package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Donhang")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonHang")
    private long madonhang;

    @OneToOne
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private User khachhang;

    @Column(name = "HoTen", nullable = false)
    private String hoten;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "DiaChi", nullable = false)
    private String diachi;

    @Column(name = "TinhThanh", nullable = false)
    private String tinhthanh;

    @Column(name = "QuanHuyen", nullable = false)
    private String quanhuyen;

    @Column(name = "NgayMua", nullable = false)
    private Date ngaymua;

    @Column(name = "SoDienThoai", nullable = false)
    private String sodienthoai;

    @Column(name = "TongTien", nullable = false)
    private long tongtien;

    @Column(name = "TrangThai", nullable = false)
    private int trangthai;
}
