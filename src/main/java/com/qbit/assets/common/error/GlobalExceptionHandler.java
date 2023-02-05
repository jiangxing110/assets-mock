package com.qbit.assets.common.error;


import com.qbit.assets.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author martinjiang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
   /* @Resource
    private Util util;*/

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidateException(MethodArgumentNotValidException e, HttpServletResponse res) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((err) -> map.put(err.getField(), err.getDefaultMessage()));
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        return R.error("参数验证错误", HttpStatus.BAD_REQUEST).put("data", map);
    }

    private R buildResult(Exception e, String message, HttpStatus status, HttpServletResponse res) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        e.printStackTrace();
        R r = new R();
        r.put("code", status.value());
        r.put("message", message);
        res.setStatus(status.value());
        //util.dealLog(request, res, r);
        return r;
    }

    @ExceptionHandler(CustomException.class)
    public R handleCustomException(CustomException e, HttpServletResponse res) {
        return buildResult(e, e.getMessage(), e.getHttpStatus(), res);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException e, HttpServletResponse res) {
        return buildResult(e, "Access denied", HttpStatus.FORBIDDEN, res);
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e, HttpServletResponse res) {
        return buildResult(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, res);
    }

}
