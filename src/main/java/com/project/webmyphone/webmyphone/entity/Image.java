package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Hinhanhsanpham")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHinhAnh")
    private long mahinhanh;

    @ManyToOne
    @JoinColumn(name = "MaSanPham", referencedColumnName = "MaSanPham", nullable = false)
    private Product sanpham;

    @Column(name = "TenHinhAnh", nullable = false)
    private String tenhinhanh;

    @Column(name = "ViTri", nullable = false)
    private int vitri;
}
