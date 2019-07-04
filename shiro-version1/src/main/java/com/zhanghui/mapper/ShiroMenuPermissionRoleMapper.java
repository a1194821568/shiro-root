package com.zhanghui.mapper;

import java.util.List;

import com.zhanghui.config.MyMapper;
import com.zhanghui.entity.ShiroMenuPermissionRole;

public interface ShiroMenuPermissionRoleMapper extends MyMapper<ShiroMenuPermissionRole>{

	List<ShiroMenuPermissionRole> selectByRoleId(Long id);

}
