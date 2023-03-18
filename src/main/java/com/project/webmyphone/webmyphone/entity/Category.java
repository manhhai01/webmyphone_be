package com.project.webmyphone.webmyphone.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Danhmuc")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhMuc")
    private long madanhmuc;

    @Column(name = "TenDanhMuc", nullable = false)
    private String tendanhmuc;

    @Column(name = "TrangThai")
    private int trangthai;
}
