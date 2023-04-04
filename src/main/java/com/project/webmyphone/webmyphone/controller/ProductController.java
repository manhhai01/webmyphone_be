package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.Regex;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.ProductDTO;
import com.project.webmyphone.webmyphone.service.ProductService;
import com.project.webmyphone.webmyphone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "*")
public class ProductController {
    ResponseMessage response;
    Regex regex;

    @Autowired
    ProductService productService;

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

    @GetMapping
    public ResponseEntity<ResponseMessage> getListProduct(@RequestParam(name = "page", defaultValue = "1") int page,
                                                          @RequestParam(name = "cate", defaultValue = "0") int cate,
                                                          @RequestParam(name = "trade", defaultValue = "0") int trade,
                                                          @RequestParam(name = "status", defaultValue = "1") int status) {
        response = new ResponseMessage();
        try {
            int quyen = checkRoleFromId();
            if (quyen == 2 && status == 0) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Yêu cầu không hợp lệ");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            Convert convert = new Convert();
            convert = productService.getListProduct(quyen, page, cate, trade, status);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin danh sách sản phẩm thành công");
            response.setData(convert.getMapData());
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{masanpham}")
    public ResponseEntity<ResponseMessage> getProductDetail(@PathVariable long masanpham) {
        response = new ResponseMessage();
        try {
            ProductDTO pDTO = productService.getProductDetail(masanpham);
            List<ProductDTO> listDTO = new ArrayList<>();
            listDTO.add(pDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin chi tiết sản phẩm thành công");
            response.setListData(listDTO);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addProduct(@RequestBody ProductDTO pDTO) {
        response = new ResponseMessage();
        try {
            if (productService.addProduct(pDTO)) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Thêm mới sản phẩm thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Sản phẩm đã tồn tại. Vui lòng kiểm tra lại");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{masanpham}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable long masanpham) {
        response = new ResponseMessage();
        try {
            productService.deleteProduct(masanpham);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Xóa sản phẩm thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{masanpham}")
    public ResponseEntity<ResponseMessage> updateProduct(@PathVariable long masanpham, @RequestBody ProductDTO pDTO) {
        response = new ResponseMessage();
        try {
            boolean check = productService.updateProduct(masanpham, pDTO);
            if (check == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Tên sản phẩm đã tồn tại. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhập thông tin sản phẩm thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
