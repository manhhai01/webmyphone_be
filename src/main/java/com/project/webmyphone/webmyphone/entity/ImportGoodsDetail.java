package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Chitietphieunhap")
public class ImportGoodsDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private long machitiet;

    @OneToOne
    @JoinColumn(name = "MaPhieuNhap", nullable = false)
    private ImportGoods phieunhap;

    @OneToOne
    @JoinColumn(name = "MaSanPham", nullable = false)
    private Product sanpham;

    @Column(name = "SoLuong", nullable = false)
    private int soluong;

    @Column(name = "DonGia", nullable = false)
    private long dongia;

    @Column(name = "ThanhTien")
    private long thanhtien;
}
