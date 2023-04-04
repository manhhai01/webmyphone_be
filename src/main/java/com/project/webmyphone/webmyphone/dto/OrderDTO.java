package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private long madonhang;
    private long mataikhoan;
    private String hoten;
    private String email;
    private String diachi;
    private String tinhthanh;
    private String quanhuyen;
    private String sodienthoai;
    private String ngaymua;
    private long tongtien;
    private List<OrderDetailDTO> chitietdonhang;
    private int trangthai;
}
