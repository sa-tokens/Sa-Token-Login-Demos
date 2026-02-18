package com.pj.stld.controller;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 
 * @author click33
 * @since 2026-02-18
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public SaResult test() {
        return SaResult.ok("ok");
    }

}

