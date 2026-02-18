package com.pj.stld.config;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 配置类：
 * 1) 开启注解鉴权（例如：{@link SaCheckLogin}）
 */
@Configuration
public class SaTokenConfigure {
    // 使用 starter 的自动配置即可，这里只负责开启注解支持
}
