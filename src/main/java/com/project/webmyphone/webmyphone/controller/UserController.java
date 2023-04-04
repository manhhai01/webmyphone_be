package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.Regex;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.UserDTO;
import com.project.webmyphone.webmyphone.service.JwtService;
import com.project.webmyphone.webmyphone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    ResponseMessage response;
    Regex regex;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    public long getIdFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return userService.getIdFromEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> checkLogin(@RequestBody UserDTO uDTO) {
        response = new ResponseMessage();
        regex = new Regex();
        String token = null;
        try {
            if (regex.regexEmail(uDTO.getEmail()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Email không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            int role = userService.checkLogin(uDTO);
            if (role == 0 || role == 1) {
                token = jwtService.createTokenAdminStaff(uDTO.getEmail());
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Đăng nhập thành công");
                Map<String, Object> data = new HashMap<>();
                data.put("Token", token);
                data.put("Role", role);
                response.setData(data);
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            } else if (role == 2) {
                token = jwtService.createTokenUser(uDTO.getEmail());
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Đăng nhập thành công");
                Map<String, Object> data = new HashMap<>();
                data.put("Token", token);
                data.put("Role", role);
                response.setData(data);
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            } else if (role == -2) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Tài khoản của bạn đã bị khóa");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Sai email hoặc password. Đăng nhập thất bại");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserDTO uDTO) {
        response = new ResponseMessage();
        regex = new Regex();
        try {
            if (regex.regexEmail(uDTO.getEmail()) == false || regex.regexSodienthoai(uDTO.getSodienthoai()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Email hoặc số điện thoại không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            if (userService.createUser(uDTO)) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.CREATED);
                response.setMessage("Đăng ký thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.CREATED);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Email hoặc số điện thoại đã được sử dụng");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/currentdetail")
    public ResponseEntity<ResponseMessage> getUserDetail() {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            UserDTO uDTO = userService.getUserDetail(mataikhoan);
            List<UserDTO> listDTO = new ArrayList<>();
            listDTO.add(uDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin tài khoản thành công");
            response.setListData(listDTO);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getListUser(@RequestParam(name = "page", defaultValue = "1") int page,
                                                       @RequestParam(name = "filter", defaultValue = "0") int filter,
                                                       @RequestParam(name = "status", defaultValue = "1") int status) {
        response = new ResponseMessage();
        try {
            Convert convert = userService.getListUser(page, filter, status);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy danh sách thông tin tài khoản thành công");
            response.setData(convert.getMapData());
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody Map<String, String> map) {
        response = new ResponseMessage();
        String password = map.get("password");
        String newPassword = map.get("newPassword");
        String rePassword = map.get("rePassword");
        try {
            long mataikhoan = getIdFromToken();
            if (StringUtils.equals(newPassword, rePassword)) {
                if (userService.changePassword(mataikhoan, password, newPassword)) {
                    response.setSuccess(true);
                    response.setStatus(HttpStatus.OK);
                    response.setMessage("Thay đổi mật khẩu thành công");
                    return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
                }
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Mật khẩu cũ không chính xác. Vui lòng nhập lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Mật khẩu mới không trùng khớp. Vui lòng kiểm tra lại");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addStaff(@RequestBody UserDTO uDTO) {
        response = new ResponseMessage();
        regex = new Regex();
        try {
            if (regex.regexEmail(uDTO.getEmail()) == false || regex.regexSodienthoai(uDTO.getSodienthoai()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Email hoặc số điện thoại không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            if (userService.createStaff(uDTO)) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.CREATED);
                response.setMessage("Thêm mới nhân viên thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.CREATED);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Email hoặc số điện thoại đã được sử dụng");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> updateUser(@RequestBody UserDTO uDTO) {
        response = new ResponseMessage();
        regex = new Regex();
        try {
            long mataikhoan = getIdFromToken();
            boolean check = userService.updateAccount(mataikhoan, uDTO);
            if (regex.regexSodienthoai(uDTO.getSodienthoai()) == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Số điện thoại không hợp lệ. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            if (check == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Số điện thoại đã được đăng ký. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhật thông tin tài khoản thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{mataikhoan}")
    public ResponseEntity<ResponseMessage> lockAccount(@PathVariable long mataikhoan) {
        response = new ResponseMessage();
        try {
            userService.lockAccount(mataikhoan);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Khóa tài khoản thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
