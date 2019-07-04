package com.zhanghui.mapper;

import java.util.List;

import com.zhanghui.config.MyMapper;
import com.zhanghui.entity.ShiroUserRole;

public interface ShiroUserRoleMapper extends MyMapper<ShiroUserRole>{

	List<ShiroUserRole> selectByUserId(Long id);

}
