package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private long mataikhoan;
    private String tenkhachhang;
    private long tongtien;
    private List<CartDetailDTO> chitietgiohang;
}
