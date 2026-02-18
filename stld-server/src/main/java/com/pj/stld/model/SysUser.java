package com.pj.stld.model;

import lombok.Data;

/**
 * 用户实体类（教学示例：极简设计）
 * 
 * @author click33
 * @since 2026-02-18
 */
@Data
public class SysUser {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 性别（可选字段）
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
