package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Thuonghieu")
public class Trademark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThuongHieu")
    private long mathuonghieu;

    @Column(name = "TenThuongHieu", nullable = false)
    private String tenthuonghieu;

    @Column(name = "TrangThai", nullable = false)
    private int trangthai;
}
