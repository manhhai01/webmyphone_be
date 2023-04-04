package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.CartDTO;
import com.project.webmyphone.webmyphone.service.CartService;
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
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {
    ResponseMessage response;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    public long getIdFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return userService.getIdFromEmail(email);
    }

    @PostMapping("/{masanpham}")
    public ResponseEntity<ResponseMessage> addCart(@PathVariable long masanpham) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            cartService.addCart(mataikhoan, masanpham);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Thêm giỏ hàng thành công");
            return getCart();
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getCart() {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            Map<String, Object> map = new HashMap<>();
            CartDTO cDTO = cartService.getCartDTO(mataikhoan);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin giỏ hàng thành công");
            map.put("mataikhoan", cDTO.getMataikhoan());
            map.put("taikhoan", cDTO.getTenkhachhang());
            map.put("tongtien", cDTO.getTongtien());
            map.put("chitietgiohang", cDTO.getChitietgiohang());
            response.setData(map);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{masanpham}")
    public ResponseEntity<ResponseMessage> deleteOneProudct(@PathVariable long masanpham) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            String name = cartService.deleteOneProduct(mataikhoan, masanpham);
            if (StringUtils.equals(name, null)) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Không thể xóa sản phẩm khỏi giỏ hàng");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Xóa thành công sản phẩm " + name + " khỏi giỏ hàng");
            // return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            return getCart();
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> updateCart(@RequestBody CartDTO cartDTO) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            cartService.updateCart(mataikhoan, cartDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhật giỏ hàng thành công");
            return getCart();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
