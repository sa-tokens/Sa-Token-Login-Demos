package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import com.pj.stld.mock.SysUserMockDao;
import com.pj.stld.mock.TempCodeMockService;
import com.pj.stld.model.SysUser;
import com.pj.stld.utils.AjaxError;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * 邮箱点击链接登录
 * 用户输入邮箱后，后端生成临时授权码并打印到控制台（模拟邮件），用户点击链接完成登录。
 *
 * @author click33
 * @since 2026-02-20
 */
@RestController
@RequestMapping("/login/email-link/")
@RequiredArgsConstructor
public class LoginByEmailLinkController {

    private final SysUserMockDao sysUserMockDao;
    private final TempCodeMockService tempCodeMockService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 发送邮件链接（模拟）
     * 生成临时授权码，构建登录链接，在控制台打印邮件内容（模拟发送）
     */
    @PostMapping("sendLink")
    public SaResult sendLink(@RequestParam String email) {
        AjaxError.notIsNull(email, "邮箱不能为空");
        String trimmedEmail = email.trim();
        AjaxError.notTrue(!EMAIL_PATTERN.matcher(trimmedEmail).matches(), "请输入正确的邮箱地址");

        SysUser user = sysUserMockDao.getByEmail(trimmedEmail);
        if (user == null) {
            AjaxError.throwMsg("该邮箱未注册，无法发送登录链接");
        }
        long userId = user.getId();
        String loginUrl = tempCodeMockService.generateAndStoreAndBuildLoginUrl(userId);

        // 模拟邮件内容，打印到控制台
        String emailContent = buildEmailContent(trimmedEmail, loginUrl);
        System.out.println("========== [模拟邮件]==========");
        System.out.println("收件人：" + trimmedEmail);
        System.out.println("主题：点击链接完成登录");
        System.out.println("内容：");
        System.out.println(emailContent);
        System.out.println("========== [模拟邮件结束] ==========");

        return SaResult.ok("登录链接已发送");
    }

    private String buildEmailContent(String email, String loginUrl) {
        return "您好！\n\n" +
                "您正在使用邮箱点击链接登录，请点击以下链接完成登录：\n\n" +
                loginUrl + "\n\n" +
                "链接有效期为 10 分钟，请勿泄露给他人。\n\n" +
                "如非本人操作，请忽略此邮件。";
    }
}
