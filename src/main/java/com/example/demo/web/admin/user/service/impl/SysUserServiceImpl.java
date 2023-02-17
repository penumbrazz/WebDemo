package com.example.demo.web.admin.user.service.impl;

import com.example.demo.web.admin.user.model.SysUser;
import com.example.demo.web.admin.user.mapper.SysUserMapper;
import com.example.demo.web.admin.user.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper mapper;
    @Override
    public List<SysUser> selectAll() {
        return mapper.selectAll();
    }
}
