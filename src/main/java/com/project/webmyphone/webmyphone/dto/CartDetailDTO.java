package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDTO {
    private long masanpham;
    private String tensanpham;
    private int soluong;
    private long dongia;
    private long thanhtien;
    private List<ImageDTO> anh;
    private int tinhtrang;
}
