package com.project.webmyphone.webmyphone.common;

import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Setter
public class ResponseMessage {
    public boolean success;
    public HttpStatus status;
    public String message;
    public Map<String, ?> data;
    public List<?> listData;

    public ResponseMessage Response500() {
        ResponseMessage response500 = new ResponseMessage();
        response500.success = false;
        response500.status = HttpStatus.INTERNAL_SERVER_ERROR;
        response500.message = "Xảy ra lỗi. Vui lòng thử lại";
        response500.data = null;
        return response500;
    }
}
