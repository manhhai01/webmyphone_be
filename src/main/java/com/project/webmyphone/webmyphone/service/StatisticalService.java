package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.dto.CategoryDTO;
import com.project.webmyphone.webmyphone.dto.ProductDTO;
import com.project.webmyphone.webmyphone.dto.StatisticalDTO;
import com.project.webmyphone.webmyphone.dto.TrademarkDTO;
import com.project.webmyphone.webmyphone.entity.*;
import com.project.webmyphone.webmyphone.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticalService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ImportGoodsRepository importRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TrademarkRepository trademarkRepository;

    public List<TrademarkDTO> getProductByTrademark() {
        List<TrademarkDTO> listDTO = new ArrayList<>();
        List<Trademark> listTrademark = trademarkRepository.findAll();
        List<Product> list = productRepository.findAll();
        TrademarkDTO tDTO;
        for (Trademark t : listTrademark) {
            int soluong = 0;
            for (Product p : list) {
                if (StringUtils.equals(t.getTenthuonghieu(), p.getThuonghieu().getTenthuonghieu())) {
                    soluong++;
                }
            }
            tDTO = new TrademarkDTO();
            tDTO.setMathuonghieu(t.getMathuonghieu());
            tDTO.setTenthuonghieu(t.getTenthuonghieu());
            tDTO.setSoluong(soluong);
            listDTO.add(tDTO);
        }
        return listDTO;
    }

    public List<CategoryDTO> getProductByCategory() {
        List<CategoryDTO> listDTO = new ArrayList<>();
        List<Category> listCategory = categoryRepository.findAll();
        List<Product> list = productRepository.findAll();
        CategoryDTO cDTO;
        for (Category c : listCategory) {
            int soluong = 0;
            for (Product p : list) {
                if (StringUtils.equals(c.getTendanhmuc(), p.getDanhmuc().getTendanhmuc())) {
                    soluong++;
                }
            }
            cDTO = new CategoryDTO();
            cDTO.setMadanhmuc(c.getMadanhmuc());
            cDTO.setTendanhmuc(c.getTendanhmuc());
            cDTO.setSoluong(soluong);
            listDTO.add(cDTO);
        }
        return listDTO;
    }

    public Date removeTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Map<String, Long> getSales(Date fromDate, Date toDate) {
        try {
            Map<String, Long> map = new HashMap<>();
            List<Order> listOrder = orderRepository.findAll();
            List<ImportGoods> listImport = importRepository.findAll();
            long sales = 0;
            if (fromDate == null && toDate == null) {
                for (Order o : listOrder) {
                    if (o.getTrangthai() == 1) {
                        sales += o.getTongtien();
                    }
                }
                for (ImportGoods ig : listImport) {
                    sales -= ig.getTongtien();
                }
                map.put("totalSales", sales);
                return map;
            } else if (fromDate != null && toDate == null) {
                System.out.println("Do day");
                for (Order o : listOrder) {
                    if (o.getTrangthai() == 1 &&
                            o.getNgaymua().after(fromDate)) {
                        sales += o.getTongtien();
                    }
                }
                for (ImportGoods ig : listImport) {
                    if (ig.getNgaynhap().after(fromDate)) {
                        sales -= ig.getTongtien();
                    }
                }
                map.put("totalSales", sales);
                return map;
            } else if (fromDate == null && toDate != null) {
                for (Order o : listOrder) {
                    if (o.getTrangthai() == 1 &&
                            o.getNgaymua().before(toDate)) {
                        sales += o.getTongtien();
                    }
                }
                for (ImportGoods ig : listImport) {
                    if (ig.getNgaynhap().before(toDate)) {
                        sales -= ig.getTongtien();
                    }
                }
                map.put("totalSales", sales);
                return map;
            } else if (fromDate != null && toDate != null) {
                if (fromDate.equals(toDate)) {
                    for (Order o : listOrder) {
                        if (o.getTrangthai() == 1 &&
                                removeTime(o.getNgaymua()).equals(fromDate)) {
                            sales += o.getTongtien();
                        }
                    }
                    for (ImportGoods ig : listImport) {
                        if (removeTime(ig.getNgaynhap()).equals(fromDate)) {
                            sales -= ig.getTongtien();
                        }
                    }
                    map.put("totalSales", sales);
                    return map;
                }
                for (Order o : listOrder) {
                    if (o.getTrangthai() == 1 &&
                            o.getNgaymua().after(fromDate)
                            && o.getNgaymua().before(toDate)) {
                        sales += o.getTongtien();
                    }
                }
                for (ImportGoods ig : listImport) {
                    if (ig.getNgaynhap().after(fromDate)
                            && ig.getNgaynhap().before(toDate)) {
                        sales -= ig.getTongtien();
                    }
                }
                map.put("totalSales", sales);
                return map;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Long> countOrder() {
        Map<String, Long> map = new HashMap<>();
        long count = orderRepository.count();
        long processed = 0;
        long pending = 0;
        long canceled = 0;
        List<Order> listOrder = orderRepository.findAll();
        map.put("totalOrders", count);
        for (Order o : listOrder) {
            if (o.getTrangthai() == 0) {
                pending++;
            } else if (o.getTrangthai() == 1) {
                processed++;
            } else if (o.getTrangthai() == 2) {
                canceled++;
            }
        }
        map.put("processedOrders", processed);
        map.put("pendingOrders", pending);
        map.put("canceledOrders", canceled);
        return map;
    }

    public List<ProductDTO> getBestSeller() {
        List<ProductDTO> listDTO = new ArrayList<>();
        List<OrderDetail> listOrderDetail = orderDetailRepository.findAll();
        List<Product> listProduct = productRepository.findAll();
        ProductDTO pDTO;
        for (Product p : listProduct) {
            pDTO = new ProductDTO();
            pDTO.setMasanpham(p.getMasanpham());
            pDTO.setTensanpham(p.getTensanpham());
            pDTO.setMadanhmuc(p.getDanhmuc().getMadanhmuc());
            pDTO.setTendanhmuc(p.getDanhmuc().getTendanhmuc());
            pDTO.setMathuonghieu(p.getThuonghieu().getMathuonghieu());
            pDTO.setTenthuonghieu(p.getThuonghieu().getTenthuonghieu());
            int soluong = 0;
            for (OrderDetail od : listOrderDetail) {
                if (od.getDonhang().getTrangthai() == 1 && od.getSanpham().getMasanpham() == p.getMasanpham()) {
                    soluong += od.getSoluong();
                }
            }
            pDTO.setGia(p.getGia());
            pDTO.setGiamgia(p.getGiamgia());
            pDTO.setSoluong(soluong);
            listDTO.add(pDTO);
        }
        List<ProductDTO> listSorted = listDTO.stream()
                .sorted(Comparator.comparing(ProductDTO::getSoluong).reversed())
                .collect(Collectors.toList());
        int top = 1;
        listDTO = new ArrayList<>();
        for (ProductDTO productDTO : listSorted) {
            if (top > 5) {
                break;
            } else if (productDTO.getSoluong() == 0) {
                break;
            }
            listDTO.add(productDTO);
            top++;
        }
        return listDTO;
    }

    public StatisticalDTO getStatistical() {
        StatisticalDTO sDTO = new StatisticalDTO();
        sDTO.setThongketheodanhmuc(getProductByCategory());
        sDTO.setThongketheothuonghieu(getProductByTrademark());
        sDTO.setThongkedoanhthu(getSales(null, null));
        sDTO.setThongketheodonhang(countOrder());
        sDTO.setThongkesanphambanchay(getBestSeller());
        return sDTO;
    }

    public StatisticalDTO getStatisticalByDate(Date fromDate, Date toDate) {
        StatisticalDTO sDTO = new StatisticalDTO();
        if (fromDate.after(toDate)) {
            return null;
        }
        sDTO.setThongketheodanhmuc(getProductByCategory());
        sDTO.setThongketheothuonghieu(getProductByTrademark());
        sDTO.setThongkedoanhthu(getSales(fromDate, toDate));
        sDTO.setThongketheodonhang(countOrder());
        sDTO.setThongkesanphambanchay(getBestSeller());
        return sDTO;
    }
}
