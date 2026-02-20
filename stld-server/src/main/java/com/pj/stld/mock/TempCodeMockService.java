package com.pj.stld.mock;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.SaManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 临时授权码模拟服务（可复用）
 * 用于邮箱点击链接登录、密码重置等场景。
 * 建立 code -> userId 的映射关系，存储至 Redis（与 Sa-Token 共用同一持久层），支持过期与一次性消费。
 *
 * @author click33
 * @since 2026-02-20
 */
@Service
public class TempCodeMockService {

    /**
     * 授权码有效期（秒），超时则失效
     */
    private static final int EXPIRE_SECONDS = 600;

    /**
     * Redis key 前缀
     */
    private static final String KEY_PREFIX = "temp:code:";

    @Value("${stld.temp-code.redirect-url:http://localhost:8080/login-pages/login-by-temp-code.html}")
    private String tempCodeRedirectUrl;

    /**
     * 生成并存储临时授权码，建立 code -> userId 的映射
     *
     * @param userId 用户 ID
     * @return 临时授权码
     */
    public String generateAndStore(Long userId) {
        String code = UUID.randomUUID().toString().replace("-", "");
        String key = KEY_PREFIX + code;
        SaManager.getSaTokenDao().set(key, String.valueOf(userId), EXPIRE_SECONDS);
        return code;
    }

    /**
     * 生成并存储临时授权码，并构建临时授权码登录页的完整 URL
     *
     * @param userId 用户 ID
     * @return 带 code 参数的登录页完整 URL
     */
    public String generateAndStoreAndBuildLoginUrl(Long userId) {
        String code = generateAndStore(userId);
        return tempCodeRedirectUrl + (tempCodeRedirectUrl.contains("?") ? "&" : "?") + "code=" + code;
    }

    /**
     * 消费并校验临时授权码，返回关联的 userId（一次性使用，验证后删除）
     *
     * @param code 临时授权码
     * @return 关联的 userId，无效或已过期返回 null
     */
    public Long consume(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        String key = KEY_PREFIX + code.trim();
        SaTokenDao dao = SaManager.getSaTokenDao();
        String stored = dao.get(key);
        if (stored != null) {
            dao.delete(key); // 一次性使用，验证成功后清除
            try {
                return Long.parseLong(stored);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
