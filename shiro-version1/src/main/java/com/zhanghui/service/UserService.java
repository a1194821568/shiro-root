package com.zhanghui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhanghui.entity.ShiroRole;
import com.zhanghui.entity.ShiroUser;
import com.zhanghui.entity.ShiroUserRole;
import com.zhanghui.mapper.ShiroUserMapper;
import com.zhanghui.mapper.ShiroUserRoleMapper;

@Service
public class UserService {

	@Autowired
	private ShiroUserMapper shiroUserMapper;
	
	@Autowired
	private ShiroUserRoleMapper shiroUserRoleMapper;
	
	public int  addUser(ShiroUser shiroUser){
		//添加用户
		int i = shiroUserMapper.insert(shiroUser);
		//判断添加用户的返回值是否正常
		if (i>0) {
			//判断集合是否有值 如果没有就不需要添加了
			if(shiroUser.getList().size()>0){
				//将前台传过来的roleid集合放进中间表的对象集合中
				for (int j = 0; j < shiroUser.getList().size(); j++) {
					//将返回的UserID放在集合中 并添加中间表
					shiroUser.getList().get(j).setUserId(shiroUser.getId());
				}
				
					shiroUserRoleMapper.insertList(shiroUser.getList());
			}
		}
		return i;
	}
}
