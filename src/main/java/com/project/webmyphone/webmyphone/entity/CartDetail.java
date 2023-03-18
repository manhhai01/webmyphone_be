package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Chitietgiohang")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private long machitiet;

    @OneToOne
    @JoinColumn(name = "MaGioHang")
    private Cart giohang;

    @OneToOne
    @JoinColumn(name = "MaSanPham")
    private Product sanpham;

    @Column(name = "SoLuong", nullable = false)
    private int soluong;

    @Column(name = "DonGia", nullable = false)
    private long dongia;

    @Column(name = "ThanhTien", nullable = false)
    private long thanhtien;
}
