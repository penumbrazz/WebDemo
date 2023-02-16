package com.example.demo.web.system.controller;


import com.example.demo.web.system.model.SysDept;
import com.example.demo.web.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门信息表 前端控制器
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
@Slf4j
@RestController
@Api(tags="SysDeptController")
@Tag(name="SysDeptController",description = "部门")
@RequestMapping("/system/sysDept")
public class SysDeptController {
    @Autowired
    private SysDeptService deptService;

    @ApiOperation("部门列表")
    @RequestMapping(value="/list",method = RequestMethod.GET)
    @ResponseBody
    public List<SysDept> list()
    {
        List<SysDept> list=deptService.list();
        log.info("info");
        log.error("error");
        log.debug("debug");
        return list;
    }
    @RequestMapping("/pathEcho/{msg}")
    @ResponseBody
    public String pathEcho(@PathVariable String msg){
        return msg;
    }
}

