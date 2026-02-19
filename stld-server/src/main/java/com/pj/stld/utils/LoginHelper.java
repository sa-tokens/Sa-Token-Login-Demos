package com.pj.stld.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.stld.model.SysUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 登录成功后的统一处理工具类
 * 供所有登录方式（账号密码、手机验证码、邮箱验证码等）在验证通过后统一调用
 *
 * @author click33
 * @since 2026-02-19
 */
public class LoginHelper {

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 执行 Sa-Token 登录，写入 Session 数据，并返回标准登录成功结果
     *
     * @param user 登录成功的用户对象
     * @return 包含 token、username 的 SaResult
     */
    public static SaResult doLoginSuccess(SysUser user) {
        // Sa-Token 登录：标记当前会话登录成功
        StpUtil.login(user.getId());
        // 写入 Session：用户信息、登录时间，供后续接口读取
        StpUtil.getSession().set(SessionConstants.USER, user);
        StpUtil.getSession().set(SessionConstants.LOGIN_TIME, LocalDateTime.now().format(DATETIME_FORMAT));
        return SaResult.ok()
                .set("token", StpUtil.getTokenValue())
                .set("username", user.getName());
    }

}
