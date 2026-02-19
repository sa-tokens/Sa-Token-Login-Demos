package com.pj.stld.config;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.fun.strategy.SaCorsHandleFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类：
 * 1) 开启注解鉴权（例如：{@link SaCheckLogin}）—— 采用拦截器模式
 * 2) CORS 跨域处理（情形2：使用 header 头提交 token 的跨域场景）
 *
 * @author click33
 * @since 2026-02-18
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能（拦截器模式）。
     * 注册后即可在 Controller 层使用 @SaCheckLogin、@SaCheckRole、@SaCheckPermission 等注解。
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * CORS 跨域处理策略（情形2：使用 header 头提交 token）。
     * 参考：<a href="https://juejin.cn/post/7491603065944129590">使用 Sa-Token CORS 策略处理跨域问题</a>
     */
    @Bean
    public SaCorsHandleFunction corsHandle() {
        return (req, res, sto) -> {
            res
                .setHeader("Access-Control-Allow-Origin", "*")
                .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                .setHeader("Access-Control-Max-Age", "3600")
                .setHeader("Access-Control-Allow-Headers", "*");

            // 如果是预检请求，则立即返回到前端
            SaRouter.match(SaHttpMethod.OPTIONS)
                .free(r -> {
                    System.out.println("--------OPTIONS预检请求，不做处理");
                })
                .back();
        };
    }
}
