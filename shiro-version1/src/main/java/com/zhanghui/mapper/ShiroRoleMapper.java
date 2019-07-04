package com.zhanghui.mapper;

import com.zhanghui.config.MyMapper;
import com.zhanghui.entity.ShiroRole;

public interface ShiroRoleMapper extends MyMapper<ShiroRole>{

	ShiroRole selectRoleById(Long id);

}
