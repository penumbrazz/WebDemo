package com.example.demo.web.admin.user.mapper;

import com.example.demo.common.elog.Elog;
import com.example.demo.web.admin.user.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Elog
    List<SysUser> selectAll();
}
