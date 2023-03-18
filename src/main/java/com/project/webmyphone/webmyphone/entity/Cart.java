package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Giohang")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGioHang")
    private long magiohang;

    @OneToOne
    @JoinColumn(name = "MaTaiKhoan")
    private User taikhoan;

    @Column(name = "TongTien", nullable = false)
    private long tongtien;
}