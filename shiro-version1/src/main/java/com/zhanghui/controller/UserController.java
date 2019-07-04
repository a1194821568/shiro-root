package com.zhanghui.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhanghui.entity.ShiroUser;
import com.zhanghui.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	/*
	 * @ApiOperation(value = “接口说明”, httpMethod = “接口请求方式”, response = “接口返回参数类型”, notes = “接口发布说明”；其他参数可参考源码； 
	 * @ApiParam(required = “是否必须参数”, name = “参数名称”, value = “参数具体描述”
	 * */
	@ApiOperation(value = "添加用户",httpMethod = "POST",notes = "添加用户:userName,loginName,password,tel,address,sex,idCard/birthday--如果有身份证号就不用添加生日")
	//@ApiParam(required = true,name = "实体类各种参数",value = "查看参数作用")
	//这里用@ApiImplicitParam 而不是用@ApiParam 是因为这个队像中含有list集合 使用上面的那个默认的方式集合没法写或者可能是我没找到方法下面的这个自己写json串传到后台
	//用上面的那个可以不用加@RequestBody 而用下面这个要加上
	@ApiImplicitParam(name = "shiroUser", value = "shiroUser信息", required = true, dataType = "ShiroUser")
	@PostMapping("/add")
	@ResponseBody
	public int  addUser(@RequestBody ShiroUser shiroUser){
		return userService.addUser(shiroUser);
	}
	
	 /**
     * 用户查询.
     * @return
     */
    @RequestMapping("/userList")
    @RequiresPermissions("6")//权限管理;
    public String userInfo(){
        return "userInfo";
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping("/userAdd")
    @RequiresPermissions("7")//权限管理;
    public String userInfoAdd(){
        return "userInfoAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @RequestMapping("/userDel")
    @RequiresPermissions("15")//权限管理;
    public String userDel(){
        return "userInfoDel";
    }

}
