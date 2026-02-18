package com.pj.stld.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.SessionConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * 账号密码登录
 * 
 * @author click33
 * @since 2026-02-18
 */
@RestController
@RequestMapping("/login/password/")
@RequiredArgsConstructor
public class LoginByPasswordController {

    private final SysUserMockDao sysUserMockDao;


    /**
     * 账号密码登录
     */
    @PostMapping("doLogin")
    public SaResult doLogin(@RequestParam String name, @RequestParam String pwd) {
        SysUser user = sysUserMockDao.getByName(name);
        if (user == null || !pwd.equals(user.getPwd())) {
            return SaResult.error("账号或密码错误");
        }

        // Sa-Token 登录：标记当前会话登录成功，参数为登录账号的唯一标识（这里使用 userId）
        StpUtil.login(user.getId());

        // 登录成功后，可往 Sa-Token Session 中写入一些业务信息，后续接口可直接读取
        StpUtil.getSession().set(SessionConstants.USER, user);
        StpUtil.getSession().set(SessionConstants.LOGIN_TIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return SaResult.ok()
            .set("token", StpUtil.getTokenValue())
            .set("username", user.getName());
    }

}

