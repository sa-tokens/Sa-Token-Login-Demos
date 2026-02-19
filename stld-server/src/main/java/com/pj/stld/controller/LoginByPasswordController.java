package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.AjaxError;
import com.pj.stld.utils.LoginHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        AjaxError.notIsNull(name, "账号不能为空");
        AjaxError.notIsNull(pwd, "密码不能为空");
        SysUser user = sysUserMockDao.getByName(name);
        if (user == null || !pwd.equals(user.getPwd())) {
            AjaxError.throwMsg("账号或密码错误");
        }

        return LoginHelper.doLoginSuccess(user);
    }

}

