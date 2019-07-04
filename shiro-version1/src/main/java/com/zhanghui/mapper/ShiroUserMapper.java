package com.zhanghui.mapper;

import com.zhanghui.config.MyMapper;
import com.zhanghui.entity.ShiroUser;

public interface ShiroUserMapper extends MyMapper<ShiroUser>{

	ShiroUser selectUserByUserName(String username);


}
