package com.example.demo.web.admin.user.controller;


import com.example.demo.web.admin.user.model.SysUser;
import com.example.demo.web.admin.user.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
@RestController
@RequestMapping("/admin/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @ApiOperation("用户列表")
    @GetMapping ("/list")
    public List<SysUser> list()
    {
        return userService.selectAll();

    }
}

