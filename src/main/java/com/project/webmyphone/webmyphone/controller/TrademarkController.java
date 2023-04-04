package com.project.webmyphone.webmyphone.controller;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.common.ResponseMessage;
import com.project.webmyphone.webmyphone.dto.TrademarkDTO;
import com.project.webmyphone.webmyphone.service.TrademarkService;
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
@RequestMapping("api/v1/trademark")
@CrossOrigin(origins = "*")
public class TrademarkController {
    ResponseMessage response;

    @Autowired
    TrademarkService trademarkService;

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
    public ResponseEntity<ResponseMessage> getListTrademark(@RequestParam(name = "page", defaultValue = "1") int page,
                                                            @RequestParam(name = "status", defaultValue = "1") int status) {
        response = new ResponseMessage();
        try {
            int quyen = checkRoleFromId();
            Convert convert = trademarkService.getListTrademark(quyen, page, status);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy danh sách thông tin thương hiêu thành công");
            if (quyen == 0 || quyen == 1) {
                response.setData(convert.getMapData());
            }
            response.setListData(convert.getListData());
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{mathuonghieu}")
    public ResponseEntity<ResponseMessage> getTrademarkById(@PathVariable long mathuonghieu) {
        response = new ResponseMessage();
        try {
            TrademarkDTO tDTO = trademarkService.getTrademarkById(mathuonghieu);
            List<TrademarkDTO> listDTO = new ArrayList<>();
            listDTO.add(tDTO);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Lấy thông tin chi tiết thương hiêu thành công");
            response.setListData(listDTO);
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addTrademark(@RequestBody TrademarkDTO tDTO) {
        response = new ResponseMessage();
        try {
            boolean check = trademarkService.addTrademark(tDTO);
            if (check == true) {
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Thêm mới thương hiệu thành công");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
            }
            response.setSuccess(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Thương hiệu đã tồn tại. Vui lòng kiếm tra lại");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{mathuonghieu}")
    public ResponseEntity<ResponseMessage> updateTrademark(@PathVariable long mathuonghieu,
                                                           @RequestBody TrademarkDTO tDTO) {
        response = new ResponseMessage();
        try {
            boolean check = trademarkService.updateTrademark(mathuonghieu, tDTO);
            if (check == false) {
                response.setSuccess(false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Tên thương hiệu đã tồn tại. Vui lòng kiểm tra lại");
                return new ResponseEntity<ResponseMessage>(response, HttpStatus.BAD_REQUEST);
            }
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Cập nhật thương hiệu thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{mathuonghieu}")
    public ResponseEntity<ResponseMessage> deleteTrademark(@PathVariable long mathuonghieu) {
        response = new ResponseMessage();
        try {
            trademarkService.deleteTrademark(mathuonghieu);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Xóa thương hiệu thành công");
            return new ResponseEntity<ResponseMessage>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(response.Response500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
