package com.project.webmyphone.webmyphone.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticalDTO {
    List<CategoryDTO> thongketheodanhmuc;
    List<TrademarkDTO> thongketheothuonghieu;
    Map<String, Long> thongketheodonhang;
    Map<String, Long> thongkedoanhthu;
    List<ProductDTO> thongkesanphambanchay;
}
