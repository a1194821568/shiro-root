package com.zhanghui.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhanghui.entity.ShiroRole;
import com.zhanghui.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@ApiOperation(value="添加角色1",notes="添加角色:roleName")
	//@ApiParam(required = true,name="添加角色3",value= "添加角色4")
	//这里用@ApiImplicitParam 而不是用@ApiParam 是因为这个队像中含有list集合 使用上面的那个默认的方式集合没法写或者可能是我没找到方法下面的这个自己写json串传到后台
	//用上面的那个可以不用加@RequestBody 而用下面这个要加上
	@ApiImplicitParam(name = "role", value = "ShiroRole信息", required = true, dataType = "ShiroRole")
	@PostMapping("/add")
	@ResponseBody
	public int addRole(@RequestBody ShiroRole role){
		return roleService.addRole(role);
	}
}
