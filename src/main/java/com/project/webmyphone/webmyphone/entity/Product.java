package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Sanpham")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSanPham")
    private long masanpham;

    @Column(name = "TenSanPham", nullable = false)
    private String tensanpham;

    @OneToOne
    @JoinColumn(name = "MaDanhMuc", nullable = false)
    private Category danhmuc;

    @OneToOne
    @JoinColumn(name = "MaThuongHieu", nullable = false)
    private Trademark thuonghieu;

    @Column(name = "SoLuong")
    private int soluong;

    @OneToMany(mappedBy = "sanpham", cascade = CascadeType.ALL)
    private List<Image> hinhanh;

    @Column(name = "MoTa", columnDefinition = "TEXT")
    private String mota;

    @Column(name = "Gia", nullable = false)
    private long gia;

    @Column(name = "GiamGia")
    private long giamgia;

    @Column(name = "TrangThai")
    private int trangthai;
}
