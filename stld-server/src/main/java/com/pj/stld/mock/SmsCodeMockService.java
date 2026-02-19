package com.pj.stld.mock;

import cn.dev33.satoken.SaManager;
import com.pj.stld.utils.AjaxError;
import cn.dev33.satoken.dao.SaTokenDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 短信验证码模拟服务（教学示例）
 * 使用 SaManager.getSaTokenDao() 将验证码存储至 Redis（与 Sa-Token 共用同一持久层）。
 * 生产环境需替换为真实短信服务商（阿里云、腾讯云等）
 *
 * @author click33
 * @since 2026-02-19
 */
@Service
public class SmsCodeMockService {

    /**
     * 验证码有效期（秒），超时则失效
     */
    private static final int EXPIRE_SECONDS = 300;

    /**
     * Redis key 前缀，避免与 Sa-Token 其它 key 冲突
     */
    private static final String KEY_PREFIX = "sms:code:";

    /**
     * 测试模式下固定验证码（便于本地联调，无需查控制台）
     */
    @Value("${stld.sms.test-mode:true}")
    private boolean testMode;

    /**
     * 生成并存储验证码，模拟“发送”短信
     *
     * @param phone 手机号
     * @return 生成的验证码（教学示例可打印到控制台方便测试，生产环境不返回）
     */
    public String sendCode(String phone) {
        AjaxError.notIsNull(phone, "手机号不能为空");
        String code = generateCode();
        String key = KEY_PREFIX + phone;

        // 对接短信服务商发送短信
        sendSmsViaProvider(phone, code);

        // 存储验证码到 Redis，用于后续校验 
        SaManager.getSaTokenDao().set(key, code, EXPIRE_SECONDS);

        System.out.println("[SmsCodeMock] 手机号 " + phone + " 的验证码：" + code + "（有效期 " + EXPIRE_SECONDS + " 秒）");

        return code;
    }

    /**
     * 校验验证码是否正确且未过期
     *
     * @param phone 手机号
     * @param code  用户输入的验证码
     * @return true=验证通过，false=验证失败或已过期
     */
    public boolean verifyCode(String phone, String code) {
        AjaxError.notIsNull(phone, "手机号不能为空");
        AjaxError.notIsNull(code, "验证码不能为空");
        String key = KEY_PREFIX + phone;
        SaTokenDao dao = SaManager.getSaTokenDao();
        String stored = dao.get(key);
        if (stored == null) {
            return false;
        }
        boolean match = code.equals(stored);
        if (match) {
            dao.delete(key); // 验证成功后清除，防止重复使用
        }
        return match;
    }

    /**
     * 生成验证码：测试模式固定为 123456，否则随机 6 位数字
     */
    private String generateCode() {
        if (testMode) {
            return "123456";
        }
        int code = (int) (Math.random() * 1_000_000);
        return String.format("%06d", code);
    }

    /**
     * 对接短信服务商发送短信
     * @param phone 手机号
     * @param code 验证码
     */
    private void sendSmsViaProvider(String phone, String code) {
        // 真实项目中：此处应对接短信服务商发送短信
    }

}
