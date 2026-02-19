package com.pj.stld.controller;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.AjaxError;
import com.pj.stld.utils.LoginHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 图形验证码登录
 * <p>
 * 流程：1. 前端请求 getCaptcha 获取验证码图片与 captchaId；
 * 2. 用户输入账号、密码、验证码；
 * 3. 提交 doLogin，后端校验验证码后执行账号密码登录。
 *
 * @author click33
 * @since 2026-02-20
 */
@RestController
@RequestMapping("/login/captcha/")
@RequiredArgsConstructor
public class LoginByCaptchaController {

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final int CAPTCHA_EXPIRE_SECONDS = 300;
    /** 验证码尺寸：与前端输入框等高（约 42px），宽度适中以清晰显示 4 位字符 */
    private static final int CAPTCHA_WIDTH = 140;
    private static final int CAPTCHA_HEIGHT = 42;
    private static final int CAPTCHA_CODE_COUNT = 4;
    private static final int CAPTCHA_INTERFERE_COUNT = 8;

    private final SysUserMockDao sysUserMockDao;

    /**
     * 生成图形验证码
     * <p>
     * 使用 Hutool CircleCaptcha 生成圆圈干扰验证码，将答案存入 Redis，返回 captchaId 与 base64 图片。
     *
     * @return 包含 captchaId、imageBase64（data URL 格式）的 SaResult
     */
    @GetMapping("getCaptcha")
    public SaResult getCaptcha() {
        // 最后一个参数 1.2f 为字体相对高度的倍数，适当放大以在低高度下保持清晰
        CircleCaptcha captcha = new CircleCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_CODE_COUNT, CAPTCHA_INTERFERE_COUNT, 1.2f);
        captcha.createCode();

        String code = captcha.getCode();
        String captchaId = IdUtil.fastSimpleUUID();

        String key = CAPTCHA_KEY_PREFIX + captchaId;
        SaManager.getSaTokenDao().set(key, code, CAPTCHA_EXPIRE_SECONDS);

        // Hutool getImageBase64Data() 已包含 data:image/png;base64, 前缀，无需重复拼接
        String imageBase64 = captcha.getImageBase64Data();

        return SaResult.ok()
                .set("captchaId", captchaId)
                .set("imageBase64", imageBase64);
    }

    /**
     * 图形验证码 + 账号密码登录
     * <p>
     * 先校验验证码，再校验账号密码，均通过则执行 Sa-Token 登录。
     *
     * @param name       账号
     * @param pwd        密码
     * @param captchaId  验证码 ID（getCaptcha 返回）
     * @param captchaCode 用户输入的验证码
     */
    @PostMapping("doLogin")
    public SaResult doLogin(
            @RequestParam String name,
            @RequestParam String pwd,
            @RequestParam String captchaId,
            @RequestParam String captchaCode) {

        AjaxError.notIsNull(name, "账号不能为空");
        AjaxError.notIsNull(pwd, "密码不能为空");
        AjaxError.notIsNull(captchaId, "验证码已失效，请刷新后重试");
        AjaxError.notIsNull(captchaCode, "请输入图形验证码");

        // 1. 校验图形验证码
        String key = CAPTCHA_KEY_PREFIX + captchaId;
        SaTokenDao dao = SaManager.getSaTokenDao();
        String stored = dao.get(key);
        if (stored == null) {
            AjaxError.throwMsg("验证码已过期，请刷新后重试");
        }
        if (!captchaCode.trim().equalsIgnoreCase(stored)) {
            AjaxError.throwMsg("图形验证码错误");
        }
        dao.delete(key);

        // 2. 校验账号密码
        SysUser user = sysUserMockDao.getByName(name);
        if (user == null || !pwd.equals(user.getPwd())) {
            AjaxError.throwMsg("账号或密码错误");
        }

        return LoginHelper.doLoginSuccess(user);
    }

}
