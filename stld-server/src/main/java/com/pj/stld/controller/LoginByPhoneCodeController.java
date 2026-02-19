package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.SmsCodeMockService;
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
 * 手机验证码登录
 *
 * @author click33
 * @since 2026-02-19
 */
@RestController
@RequestMapping("/login/phone-code/")
@RequiredArgsConstructor
public class LoginByPhoneCodeController {

    private final SysUserMockDao sysUserMockDao;
    private final SmsCodeMockService smsCodeMockService;

    /**
     * 中国大陆手机号简单校验正则（11 位，1 开头）
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 发送短信验证码
     */
    @PostMapping("sendCode")
    public SaResult sendCode(@RequestParam String phone) {
        AjaxError.notIsNull(phone, "手机号不能为空");
        String trimmedPhone = phone.trim();
        AjaxError.notTrue(!PHONE_PATTERN.matcher(trimmedPhone).matches(), "请输入正确的手机号");

        // 判断该手机号是否已注册
        SysUser user = sysUserMockDao.getByPhone(trimmedPhone);
        if (user == null) {
            AjaxError.throwMsg("该手机号未注册，无法发送验证码");
        }

        smsCodeMockService.sendCode(trimmedPhone);
        return SaResult.ok("验证码已发送");
    }

    /**
     * 手机号 + 验证码登录
     */
    @PostMapping("doLogin")
    public SaResult doLogin(@RequestParam String phone, @RequestParam String code) {
        AjaxError.notIsNull(phone, "手机号不能为空");
        AjaxError.notIsNull(code == null ? null : (code.trim().isEmpty() ? "" : code), "请输入验证码");
        String trimmedPhone = phone.trim();
        String trimmedCode = code.trim();
        AjaxError.notTrue(!PHONE_PATTERN.matcher(trimmedPhone).matches(), "请输入正确的手机号");

        if (!smsCodeMockService.verifyCode(trimmedPhone, trimmedCode)) {
            AjaxError.throwMsg("验证码错误或已过期，请重新获取");
        }

        SysUser user = sysUserMockDao.getByPhone(trimmedPhone);
        if (user == null) {
            AjaxError.throwMsg("该手机号未注册");
        }

        return LoginHelper.doLoginSuccess(user);
    }
}
