package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.ImportGoodsDTO;
import com.project.webmyphone.webmyphone.service.ImportGoodsService;
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
@RequestMapping("/api/v1/import")
@CrossOrigin(origins = "*")
public class ImportGoodsController {
    ResponseMessage response;

    @Autowired
    ImportGoodsService importService;

    @Autowired
    UserService userService;

    public long getIdFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return userService.getIdFromEmail(email);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getListImport(@RequestParam(name = "page", defaultValue = "1") int page) {
        response = new ResponseMessage();
        try {
            Convert convert = new Convert();
            convert = importService.getListImport(page);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy danh sách thông tin phiếu nhập thành công");
            response.setData(convert.getMapData());
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{maphieunhap}")
    public ResponseEntity<ResponseMessage> getImportDetail(@PathVariable long maphieunhap) {
        response = new ResponseMessage();
        try {
            ImportGoodsDTO igDTO = importService.getImportDetail(maphieunhap);
            List<ImportGoodsDTO> listDTO = new ArrayList<>();
            listDTO.add(igDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin chi tiết phiếu nhập thành công");
            response.setListData(listDTO);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addImportGoods(@RequestBody ImportGoodsDTO importDTO) {
        response = new ResponseMessage();
        try {
            long mataikhoan = getIdFromToken();
            importService.addImportGoods(mataikhoan, importDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Nhập hàng thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
