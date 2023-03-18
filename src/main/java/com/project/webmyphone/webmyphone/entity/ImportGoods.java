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
@Table(name = "Phieunhap")
public class ImportGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhieuNhap")
    private long maphieunhap;

    @OneToOne
    @JoinColumn(name = "MaNhanVien")
    private User nhanvien;

    @Column(name = "NgayNhap", nullable = false)
    private Date ngaynhap;

    @Column(name = "TongTien", nullable = false)
    private long tongtien;
}
