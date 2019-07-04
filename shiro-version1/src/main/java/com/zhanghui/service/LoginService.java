package com.zhanghui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.zhanghui.entity.ShiroUser;
import com.zhanghui.entity.ShiroUserRole;
import com.zhanghui.mapper.ShiroUserMapper;
import com.zhanghui.mapper.ShiroUserRoleMapper;

@Service
public class LoginService{

	@Autowired
	private ShiroUserMapper shiroUserMapper;
	
	@Autowired
	private ShiroUserRoleMapper shiroUserRoleMapper;
	
	public ShiroUser findByUsername(String username) {
		// TODO 自动生成的方法存根
		//根据登录名去找用户信息
		//ShiroUser user = shiroUserMapper.selectByPrimaryKey(7);
		//通过在xml中写sql获取用户的基本信息
		ShiroUser user = shiroUserMapper.selectUserByUserName(username);
		//通过写sql获取用户对应角色的中间表
		List<ShiroUserRole> list = shiroUserRoleMapper.selectByUserId(user.getId());
		user.setList(list);
		System.out.println("list 长度==》"+list.size());
		/*Example example = new Example(LoginService.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("loginName",username);
		ShiroUser user = shiroUserMapper.selectOneByExample(example);*/
		return user;
	}

	
}
