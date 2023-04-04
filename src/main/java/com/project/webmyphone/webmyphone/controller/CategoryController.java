package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.CategoryDTO;
import com.project.webmyphone.webmyphone.service.CategoryService;
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
@RequestMapping("api/v1/category")
@CrossOrigin(origins = "*")
public class CategoryController {
    ResponseMessage response;

    @Autowired
    CategoryService categoryService;

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
    public ResponseEntity<ResponseMessage> getListCategory(@RequestParam(name = "page", defaultValue = "1") int page,
                                                           @RequestParam(name = "status", defaultValue = "1") int status) {
        response = new ResponseMessage();
        try {
            int quyen = checkRoleFromId();
            Convert convert = categoryService.getListCategory(quyen, page, status);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy danh sách thông tin danh mục thành công");
            if (quyen == 0 || quyen == 1) {
                response.setData(convert.getMapData());
            }
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{madanhmuc}")
    public ResponseEntity<ResponseMessage> getCategoryById(@PathVariable long madanhmuc) {
        response = new ResponseMessage();
        try {
            CategoryDTO cDTO = categoryService.getCategoryById(madanhmuc);
            List<CategoryDTO> listDTO = new ArrayList<>();
            listDTO.add(cDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin chi tiết danh mục thành công");
            response.setListData(listDTO);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addCategory(@RequestBody CategoryDTO cDTO) {
        response = new ResponseMessage();
        try {
            boolean check = categoryService.addCategory(cDTO);
            if (check == true) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Thêm mới danh mục thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Danh mục đã tồn tại. Vui lòng kiếm tra lại");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{madanhmuc}")
    public ResponseEntity<ResponseMessage> updateCategory(@PathVariable long madanhmuc, @RequestBody CategoryDTO cDTO) {
        response = new ResponseMessage();
        try {
            boolean check = categoryService.updateCategory(madanhmuc, cDTO);
            if (check == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Tên danh mục đã tồn tại. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhật danh mục thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{madanhmuc}")
    public ResponseEntity<ResponseMessage> deleteCategory(@PathVariable long madanhmuc) {
        response = new ResponseMessage();
        try {
            categoryService.deleteCategory(madanhmuc);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Xóa danh mục thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
