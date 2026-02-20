package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.mock.TempCodeMockService;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.AjaxError;
import com.pj.stld.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 临时授权码登录（可复用）
 * 供邮箱点击链接登录、密码重置等场景使用。
 * 通过临时授权码完成登录，授权码由后端生成并存储，一次性有效。
 *
 * @author click33
 * @since 2026-02-20
 */
@RestController
@RequestMapping("/login/temp-code/")
@RequiredArgsConstructor
public class LoginByTempCodeController {

    private final SysUserMockDao sysUserMockDao;
    private final TempCodeMockService tempCodeMockService;

    /**
     * 使用临时授权码登录
     */
    @PostMapping("doLogin")
    public SaResult doLogin(@RequestParam String code) {
        AjaxError.notIsNull(code, "授权码不能为空");
        String trimmedCode = code.trim();
        AjaxError.notTrue(trimmedCode.isEmpty(), "授权码不能为空");

        Long userId = tempCodeMockService.consume(trimmedCode);
        if (userId == null) {
            AjaxError.throwMsg("授权码无效或已过期，请重新获取");
        }

        SysUser user = sysUserMockDao.getById(userId);
        if (user == null) {
            AjaxError.throwMsg("用户不存在");
        }

        return LoginHelper.doLoginSuccess(user);
    }
}
