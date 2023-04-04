package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportGoodsDTO {
    private long maphieunhap;
    private long manhanvien;
    private String tennhanvien;
    private String ngaynhap;
    private long tongtien;
    private List<ImportGoodsDetailDTO> chitietphieunhap;
}
