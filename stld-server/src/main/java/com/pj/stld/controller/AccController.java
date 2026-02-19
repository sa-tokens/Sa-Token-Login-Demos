package com.pj.stld.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.SessionConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关接口（框架用）
 * 供前端 index.html 等页面调用，用于检查登录状态、获取用户信息、注销等
 * 
 * @author click33
 * @since 2026-02-18
 */
@RestController
@RequestMapping("/acc/")
public class AccController {

    /**
     * 获取当前登录用户信息（返回当前 SysUser，pwd 字段脱敏）
     * 未登录时由 GlobalExceptionHandler 返回 401
     */
    @SaCheckLogin
    @PostMapping("currUserInfo")
    public SaResult currUserInfo() {
        SysUser user = (SysUser) StpUtil.getSession().get(SessionConstants.USER);
        if (user == null) {
            // 理论不会出现，保险起见可特殊处理
            return SaResult.error("用户信息不存在");
        }
        // pwd 字段脱敏，其余字段全部拷贝
        SysUser safeUser = user.copy();
        safeUser.setPwd("******");

        return SaResult.data(safeUser);
    }

    /**
     * 注销登录
     */
    @SaCheckLogin
    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
