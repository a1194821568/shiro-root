package com.zhanghui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhanghui.entity.ShiroMenu;
import com.zhanghui.mapper.ShiroMenuMapper;

@Service
public class MenuService {

	@Autowired
	private ShiroMenuMapper shiroMenuMapper;
	
	public int addMenu (ShiroMenu record){
		int i = shiroMenuMapper.insert(record);
		System.out.println(record.getId());
		return i;
	}

	public int updateMenu(ShiroMenu menu) {
		// TODO 自动生成的方法存根
		
		return shiroMenuMapper.updateByPrimaryKeySelective(menu);
	}

	public int delMenu(Long id) {
		// TODO 自动生成的方法存根
		return shiroMenuMapper.deleteByPrimaryKey(id);
	}
}
