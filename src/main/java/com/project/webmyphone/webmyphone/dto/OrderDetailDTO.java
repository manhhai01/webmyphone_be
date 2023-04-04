package com.project.webmyphone.webmyphone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private long masanpham;
    private String tensanpham;
    private int soluong;
    private long dongia;
    private long thanhtien;
}
