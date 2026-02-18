package com.pj.stld.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.SessionConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关接口（框架用）
 * 供前端 index.html 等页面调用，用于检查登录状态、获取用户信息、注销等
 * 
 * @author click33
 * @since 2026-02-18
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 获取当前登录用户信息
     * 未登录时由 GlobalExceptionHandler 返回 401
     */
    @SaCheckLogin
    @GetMapping("/info")
    public SaResult info() {
        Object loginId = StpUtil.getLoginId();
        // 可从 Session 中取更多信息，各登录 Controller 登录成功后会写入
        SysUser user = (SysUser) StpUtil.getSession().get(SessionConstants.USER);
        Object loginTime = StpUtil.getSession().get(SessionConstants.LOGIN_TIME);

        Map<String, Object> data = new HashMap<>();
        data.put("username", user != null ? user.getName() : loginId.toString());
        data.put("loginTime", loginTime != null ? loginTime : "-");
        return SaResult.data(data);
    }

    /**
     * 注销登录
     */
    @SaCheckLogin
    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
