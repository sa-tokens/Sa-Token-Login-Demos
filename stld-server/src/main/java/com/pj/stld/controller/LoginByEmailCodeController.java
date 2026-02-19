package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.EmailCodeMockService;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.AjaxError;
import com.pj.stld.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * 邮箱验证码登录
 *
 * @author click33
 * @since 2026-02-20
 */
@RestController
@RequestMapping("/login/email-code/")
@RequiredArgsConstructor
public class LoginByEmailCodeController {

    private final SysUserMockDao sysUserMockDao;
    private final EmailCodeMockService emailCodeMockService;

    /**
     * 邮箱格式简单校验正则（常见格式）
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 发送邮件验证码
     */
    @PostMapping("sendCode")
    public SaResult sendCode(@RequestParam String email) {
        AjaxError.notIsNull(email, "邮箱不能为空");
        String trimmedEmail = email.trim();
        AjaxError.notTrue(!EMAIL_PATTERN.matcher(trimmedEmail).matches(), "请输入正确的邮箱地址");

        // 判断该邮箱是否已注册
        SysUser user = sysUserMockDao.getByEmail(trimmedEmail);
        if (user == null) {
            AjaxError.throwMsg("该邮箱未注册，无法发送验证码");
        }

        emailCodeMockService.sendCode(trimmedEmail);
        return SaResult.ok("验证码已发送");
    }

    /**
     * 邮箱 + 验证码登录
     */
    @PostMapping("doLogin")
    public SaResult doLogin(@RequestParam String email, @RequestParam String code) {
        AjaxError.notIsNull(email, "邮箱不能为空");
        AjaxError.notIsNull(code, "请输入验证码");
        String trimmedEmail = email.trim();
        String trimmedCode = code.trim();
        AjaxError.notTrue(trimmedCode.isEmpty(), "请输入验证码");
        AjaxError.notTrue(!EMAIL_PATTERN.matcher(trimmedEmail).matches(), "请输入正确的邮箱地址");

        if (!emailCodeMockService.verifyCode(trimmedEmail, trimmedCode)) {
            AjaxError.throwMsg("验证码错误或已过期，请重新获取");
        }

        SysUser user = sysUserMockDao.getByEmail(trimmedEmail);
        if (user == null) {
            AjaxError.throwMsg("该邮箱未注册");
        }

        return LoginHelper.doLoginSuccess(user);
    }
}
