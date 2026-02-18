package com.pj.stld.mock;

import com.pj.stld.model.SysUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据模拟层（模拟数据库查询操作）
 * 教学示例：使用内存数据模拟数据库，避免引入真实数据库依赖
 * 
 * @author click33
 * @since 2026-02-18
 */
@Component
public class SysUserMockDao {

    /**
     * 模拟用户数据列表（内存存储）
     */
    private static final List<SysUser> USER_LIST = new ArrayList<>();

    /**
     * 初始化模拟用户数据
     */
    static {
        // 用户1
        SysUser user1 = new SysUser();
        user1.setId(1L);
        user1.setName("zhangsan");
        user1.setPwd("123456");
        user1.setGender("男");
        user1.setAge(25);
        user1.setPhone("13800138001");
        user1.setEmail("zhangsan@sa.com");
        USER_LIST.add(user1);

        // 用户2
        SysUser user2 = new SysUser();
        user2.setId(2L);
        user2.setName("wangwu");
        user2.setPwd("123456");
        user2.setGender("女");
        user2.setAge(28);
        user2.setPhone("13800138002");
        user2.setEmail("wangwu@sa.com");
        USER_LIST.add(user2);

        // 用户3
        SysUser user3 = new SysUser();
        user3.setId(3L);
        user3.setName("lisi");
        user3.setPwd("123456");
        user3.setGender("男");
        user3.setAge(30);
        user3.setPhone("13800138003");
        user3.setEmail("lisi@sa.com");
        USER_LIST.add(user3);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param name 用户名
     * @return 用户对象，未找到返回 null
     */
    public SysUser getByName(String name) {
        return USER_LIST.stream()
                .filter(user -> name != null && name.equals(user.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户对象，未找到返回 null
     */
    public SysUser getByPhone(String phone) {
        return USER_LIST.stream()
                .filter(user -> phone != null && phone.equals(user.getPhone()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象，未找到返回 null
     */
    public SysUser getByEmail(String email) {
        return USER_LIST.stream()
                .filter(user -> email != null && email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据用户 ID 查询用户
     * 
     * @param id 用户 ID
     * @return 用户对象，未找到返回 null
     */
    public SysUser getById(Long id) {
        return USER_LIST.stream()
                .filter(user -> id != null && id.equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取所有用户列表
     * 
     * @return 用户列表
     */
    public List<SysUser> getAll() {
        return new ArrayList<>(USER_LIST);
    }

}
