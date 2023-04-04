package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private long masanpham;
    private String tensanpham;
    private String mota;
    private int soluong;
    private List<ImageDTO> anh;
    private long mathuonghieu;
    private String tenthuonghieu;
    private long madanhmuc;
    private String tendanhmuc;
    private long gia;
    private long giamgia;
}
