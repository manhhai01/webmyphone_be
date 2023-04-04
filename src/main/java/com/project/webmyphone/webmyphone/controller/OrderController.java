package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.Regex;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.OrderDTO;
import com.project.webmyphone.webmyphone.service.OrderService;
import com.project.webmyphone.webmyphone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "*")
public class OrderController {
    ResponseMessage response;
    Regex regex;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    public long getIdFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return userService.getIdFromEmail(email);
    }

    public int checkRoleFromId() {
        return userService.checkRoleFromId(getIdFromToken());
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addOrder(@RequestBody OrderDTO oDTO) {
        response = new ResponseMessage();
        regex = new Regex();
        try {
            long mataikhoan = getIdFromToken();
            if (regex.regexEmail(oDTO.getEmail()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Email không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            if (regex.regexSodienthoai(oDTO.getSodienthoai()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Số điện thoại nhận hàng không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            String name = orderService.addOrder(mataikhoan, oDTO);
            if (StringUtils.equals(name, "0")) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Giỏ hàng chưa có sản phẩm. Không thể thanh toán");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            } else if (StringUtils.equals(name, "1")) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Đặt hàng thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Sản phẩm " + name + " không còn được phục vụ");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{madonhang}")
    public ResponseEntity<ResponseMessage> updateStatus(@PathVariable long madonhang) {
        response = new ResponseMessage();
        try {
            int quyen = checkRoleFromId();
            int check = orderService.updateStatus(quyen, madonhang);
            if (check == 0) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Đơn hàng đã hoàn thành không thể thay đổi");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            } else if (check == 1) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Đơn hàng không còn tồn tại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            } else if (check == 2) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Số lượng sản phẩm không đủ cung cấp");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            } else if (check == 4) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Nhân viên không thể thực hiện thao tác này");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhật tình trạng đơn hàng thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{madonhang}")
    public ResponseEntity<ResponseMessage> deleteOrder(@PathVariable long madonhang) {
        response = new ResponseMessage();
        try {
            int quyen = checkRoleFromId();
            if (quyen == 2) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Nhân viên không thể hủy đơn hàng");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            boolean check = orderService.deleteOrder(madonhang);
            if (check == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Đơn hàng đã xử lý không thể hủy");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Hủy đơn hàng thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getListOrder(@RequestParam(name = "page", defaultValue = "1") int page,
                                                        @RequestParam(name = "filter", defaultValue = "0") int filter) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            int quyen = checkRoleFromId();
            Convert convert = new Convert();
            if (quyen == 0) {
                convert = orderService.getListforAdmin(page, filter);
            } else if (quyen == 1) {
                if (filter == 0 || filter == 4 || filter == 5) {
                    response.setSuccess(false);
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage("Nhân viên không có quyền với chức năng này");
                    return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
                }
                convert = orderService.getListforStaff(page, filter);
            } else if (quyen == 2) {
                convert = orderService.getListforUser(mataikhoan, page, filter);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin danh sách đơn hàng thành công");
            response.setData(convert.getMapData());
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{madonhang}")
    public ResponseEntity<ResponseMessage> getOrderDetail(@PathVariable long madonhang) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            int quyen = checkRoleFromId();
            OrderDTO oDTO = orderService.getOrderDetail(madonhang);
            if (quyen == 2) {
                if (oDTO.getMataikhoan() != mataikhoan) {
                    response.setSuccess(false);
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage("Không thể lấy thông tin chi tiết đơn hàng người khác");
                    return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
                }
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin chi tiết đơn hàng thành công");
            Map<String, Object> map = new HashMap<>();
            map.put("Account", oDTO.getEmail());
            map.put("Receiver", oDTO.getHoten());
            map.put("deliveryAddress", oDTO.getDiachi() + " " + oDTO.getQuanhuyen() + " " + oDTO.getTinhthanh());
            map.put("totalPrice", oDTO.getTongtien());
            map.put("detailOrder", oDTO.getChitietdonhang());
            response.setData(map);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
