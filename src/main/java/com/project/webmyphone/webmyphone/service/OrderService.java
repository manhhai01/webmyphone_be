package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.OrderDTO;
import com.project.webmyphone.webmyphone.entity.Cart;
import com.project.webmyphone.webmyphone.entity.Order;
import com.project.webmyphone.webmyphone.repository.OrderRepository;
import com.project.webmyphone.webmyphone.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    Convert convert;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    CartService cartService;

    public String addOrder(long mataikhoan, OrderDTO oDTO) {
        Cart c = cartService.getCart(mataikhoan);
        if (c == null) {
            return "0";
        }
        String name = orderDetailService.checkProduct(c.getMagiohang());
        if (StringUtils.equals(name, null)) {
            Order o = new Order();
            o.setKhachhang(userRepository.findById(mataikhoan).get());
            o.setHoten(oDTO.getHoten());
            o.setEmail(oDTO.getEmail());
            o.setSodienthoai(oDTO.getSodienthoai());
            o.setDiachi(oDTO.getDiachi());
            o.setTinhthanh(oDTO.getTinhthanh());
            o.setQuanhuyen(oDTO.getQuanhuyen());
            o.setNgaymua(new Date());
            o.setTongtien(c.getTongtien());
            o.setTrangthai(0);
            orderRepository.save(o);
            orderDetailService.addOrderDetail(o.getMadonhang(), c.getMagiohang());
            cartService.deleteCart(mataikhoan);
            return "1";
        }
        return name;
    }

    public int updateStatus(int quyen, long madonhang) {
        Order o = orderRepository.findById(madonhang).get();
        if (o.getTrangthai() == 4) {
            return 0;
        } else if (o.getTrangthai() == 5) {
            return 1;
        } else if (quyen == 1) {
            if (o.getTrangthai() == 0 || o.getTrangthai() == 3) {
                return 4;
            }
        } else if (o.getTrangthai() == 0) {
            boolean check = orderDetailService.updateQuantity(madonhang);
            if (check == false) {
                return 2;
            }
        }
        o.setTrangthai(o.getTrangthai() + 1);
        orderRepository.save(o);
        return 3;
    }

    public boolean deleteOrder(long madonhang) {
        Order o = orderRepository.findById(madonhang).get();
        if (o.getTrangthai() != 0) {
            return false;
        }
        o.setTrangthai(5);
        orderRepository.save(o);
        return true;
    }

    public List<Order> getListOrdersUser(long mataikhoan, int filter) {
        List<Order> list = orderRepository.findAll();
        List<Order> listOrder = new ArrayList<>();
        for (Order o : list) {
            if (filter == 3) {
                if (o.getKhachhang().getMataikhoan() == mataikhoan) {
                    listOrder.add(o);
                }
            } else if (filter != 3) {
                if (o.getKhachhang().getMataikhoan() == mataikhoan && o.getTrangthai() == filter) {
                    listOrder.add(o);
                }
            }
        }
        return listOrder;
    }

    public List<Order> getListOrdersAdmin(int filter) {
        List<Order> list = orderRepository.findAll();
        List<Order> listOrder = new ArrayList<>();
        for (Order o : list) {
            if (o.getTrangthai() == filter) {
                listOrder.add(o);
            }
        }
        return listOrder;
    }

    public List<Order> getListOrdersStaff(int filter) {
        List<Order> list = orderRepository.findAll();
        List<Order> listOrder = new ArrayList<>();
        if (filter == 6) {
            for (Order o : list) {
                if (o.getTrangthai() == 1 || o.getTrangthai() == 2 || o.getTrangthai() == 3) {
                    listOrder.add(o);
                }
            }
        } else if (filter != 6) {
            for (Order o : list) {
                if (o.getTrangthai() == filter) {
                    listOrder.add(o);
                }
            }
        }
        return listOrder;
    }

    public List<OrderDTO> parseDTO(List<Order> list) {
        List<OrderDTO> listDTO = new ArrayList<>();
        OrderDTO oDTO;
        for (Order o : list) {
            oDTO = new OrderDTO();
            oDTO.setMadonhang(o.getMadonhang());
            oDTO.setMataikhoan(o.getKhachhang().getMataikhoan());
            oDTO.setHoten(o.getHoten());
            oDTO.setEmail(o.getEmail());
            oDTO.setDiachi(o.getDiachi());
            oDTO.setTinhthanh(o.getTinhthanh());
            oDTO.setQuanhuyen(o.getQuanhuyen());
            oDTO.setSodienthoai(o.getSodienthoai());
            oDTO.setNgaymua(format.format(o.getNgaymua()));
            oDTO.setTongtien(o.getTongtien());
            oDTO.setTrangthai(o.getTrangthai());
            listDTO.add(oDTO);
        }
        return listDTO;
    }

    public Convert getListforUser(long mataikhoan, int page, int filter) {
        List<Order> list = new ArrayList<>();
        list = getListOrdersUser(mataikhoan, filter);
        convert = convert.orderPage(list, page);
        List<Order> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Order>(), Order.class);
        List<OrderDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public Convert getListforAdmin(int page, int filter) {
        List<Order> list = new ArrayList<>();
        if (filter == 6) {
            list = orderRepository.findAll();
            convert = convert.orderPage(list, page);
        } else if (filter != 6) {
            list = getListOrdersAdmin(filter);
            convert = convert.orderPage(list, page);
        }
        List<Order> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Order>(), Order.class);
        List<OrderDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public Convert getListforStaff(int page, int filter) {
        List<Order> list = new ArrayList<>();
        list = getListOrdersStaff(filter);
        convert = convert.orderPage(list, page);
        List<Order> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Order>(), Order.class);
        List<OrderDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public OrderDTO getOrderDetail(long madonhang) {
        Order o = orderRepository.findById(madonhang).get();
        OrderDTO oDTO = new OrderDTO();
        oDTO = new OrderDTO();
        oDTO.setMadonhang(o.getMadonhang());
        oDTO.setMataikhoan(o.getKhachhang().getMataikhoan());
        oDTO.setHoten(o.getHoten());
        oDTO.setEmail(o.getEmail());
        oDTO.setDiachi(o.getDiachi());
        oDTO.setTinhthanh(o.getTinhthanh());
        oDTO.setQuanhuyen(o.getQuanhuyen());
        oDTO.setNgaymua(format.format(o.getNgaymua()));
        oDTO.setTongtien(o.getTongtien());
        oDTO.setChitietdonhang(orderDetailService.getListDetail(madonhang));
        oDTO.setTrangthai(o.getTrangthai());
        return oDTO;
    }
}
