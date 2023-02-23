package com.example.demo.web.system.controller;


import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.example.demo.web.system.model.SysDept;
import com.example.demo.web.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @CachePut(value="demo",key="':level:level1:level2'")
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
    @Cacheable(value = "echo",key="'echokey'")
    public String pathEcho(@PathVariable String msg){
        return msg;
    }

    @RequestMapping(value="/deptList",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> deptList()
    {
        String sql="select * from sys_dept";
        List<Map<String,Object>> list= SqlRunner.db().selectList(sql);
        return list;
    }
}

