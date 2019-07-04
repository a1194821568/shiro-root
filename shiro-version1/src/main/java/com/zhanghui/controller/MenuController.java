package com.zhanghui.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.noinline;

import com.zhanghui.entity.ShiroMenu;
import com.zhanghui.service.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private MenuService  menuService;
	
	@ApiOperation(value="添加资源",notes="添加资源,需要添加menuName，menuUrl,MenuSort,parent,permission，menuType")
	@ApiParam(value="添加资源",name="添加资源")
	@PostMapping("/add")
	@ResponseBody
	public int addMenu(ShiroMenu menu){
		return menuService.addMenu(menu);
	}

	@ApiOperation(value="修改资源",notes="修改资源,能修改的有menuName，menuUrl,MenuSort,parent,permission，menuType")
	@ApiParam(value="添加资源",name="添加资源")
	@PatchMapping("/update")
	@ResponseBody
	public int updateMenu(ShiroMenu menu){
		return menuService.updateMenu(menu);
	}
	
	@ApiOperation(value="删除资源",notes="删除资源 ： id")
	@ApiParam(value="添加资源",name="添加资源")
	@Delete("/del")
	@ResponseBody
	public int delMenu(Long id){
		return menuService.delMenu(id);
	}
}
