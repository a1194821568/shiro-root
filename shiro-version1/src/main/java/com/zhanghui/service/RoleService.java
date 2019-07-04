package com.zhanghui.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scala.collection.parallel.mutable.ParTrieMap.Size;
import tk.mybatis.mapper.entity.Example;

import tk.mybatis.mapper.entity.Example.Criteria;
import com.zhanghui.entity.ShiroMenuPermissionRole;
import com.zhanghui.entity.ShiroRole;
import com.zhanghui.mapper.ShiroMenuPermissionRoleMapper;
import com.zhanghui.mapper.ShiroRoleMapper;

@Service
public class RoleService {
	@Autowired
	private ShiroRoleMapper shiroRoleMapper;
	@Autowired
	private ShiroMenuPermissionRoleMapper shiroMenuPermissionRoleMapper;
	

	public int addRole(ShiroRole role) {
		// TODO 自动生成的方法存根
		//添加角色
		int i = shiroRoleMapper.insert(role);
		System.out.println(role.getId());
		//判断角色实体是否添加上 如果添加上接着添加角色与资源对应的中间表
		List<ShiroMenuPermissionRole> list = new ArrayList<ShiroMenuPermissionRole>();
		if(i > 0){
			for (int j = 0; j < role.getList().size(); j++) {
				ShiroMenuPermissionRole mpr = new ShiroMenuPermissionRole();
				mpr.setRoleId(role.getId());//角色添加的数据可以返回这条数据的ID
				mpr.setMenuId(role.getList().get(j).getMenuId());
				//其他创建时间等省略。。。。。。
				
				list.add(mpr);
			}
			//添加角色资源中间表
			shiroMenuPermissionRoleMapper.insertList(list);
		}
		return i;
	}


	public ShiroRole selectRoleByid(Long id) {
		// TODO 自动生成的方法存根
		//根据id获得中间表的资源集合 并放到角色对象中
		ShiroRole role = shiroRoleMapper.selectByPrimaryKey(id);
		//这个可以直接使用通用mapper根据主键查询 下面这个备用 已经在xml中写好sql了
		//ShiroRole role = shiroRoleMapper.selectRoleById(id);
		
		//跟胡roleId查询menu的id 的中间表 返回list 并放在对象中
		List<ShiroMenuPermissionRole> list = shiroMenuPermissionRoleMapper.selectByRoleId(id);
		role.setList(list);
	/*	Example example = new Example(ShiroMenuPermissionRole.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("roleId",id);
		List<ShiroMenuPermissionRole> list = shiroMenuPermissionRoleMapper.selectByExample(example);
		ShiroRole role = new ShiroRole();
		if(list.size() > 0){
			role.setList(list);
		}*/
		return role;
	}

}
