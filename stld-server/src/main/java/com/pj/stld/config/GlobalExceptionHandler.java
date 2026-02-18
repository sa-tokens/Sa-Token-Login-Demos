package com.pj.stld.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public SaResult handleNotLogin(NotLoginException e) {
        // 未登录异常：通常由鉴权（如 StpUtil.checkLogin / @SaCheckLogin）触发
        return SaResult.error("未登录").setCode(401).setMsg(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public SaResult handleException(Exception e) {
        return SaResult.error(e.getMessage());
    }

}

