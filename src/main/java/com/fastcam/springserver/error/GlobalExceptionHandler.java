package com.fastcam.springserver.error;

import com.fastcam.springserver.entity.AdminError;
import com.fastcam.springserver.service.AdminErrorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    AdminErrorService aes;

    @ExceptionHandler(Exception.class)
    public void handleException(
            Exception e,
            HttpServletRequest request
    ) {

        AdminError error = new AdminError();

        error.setTime(LocalDateTime.now());

        error.setType(
                e.getClass().getSimpleName()
        );

        error.setLevel("ERROR");

        error.setMethod(
                request.getMethod()
        );

        error.setApi(
                request.getRequestURI()
        );

        error.setMsg(
                e.getMessage()
        );

        error.setStatusCode(500);

        aes.saveError(error);
    }
}