package com.zhanghui.config;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;







import org.springframework.beans.factory.annotation.Autowired;

import com.zhanghui.entity.ShiroMenuPermissionRole;
import com.zhanghui.entity.ShiroRole;
import com.zhanghui.entity.ShiroUser;
import com.zhanghui.entity.ShiroUserRole;
import com.zhanghui.mapper.ShiroRoleMapper;
import com.zhanghui.service.LoginService;
import com.zhanghui.service.RoleService;


public class MyShiroRealm extends AuthorizingRealm{
	@Resource
	private LoginService loginService;
	
	@Autowired
	private RoleService roleService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO 自动生成的方法存根
		 System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
		 SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		 ShiroUser userInfo  = (ShiroUser)principals.getPrimaryPrincipal();
		 
		 for(ShiroUserRole shiro:userInfo.getList()){
			 	//这个是我在中间表定义的需找到角色表的对应的数据还需要将资源角色中间表的数据查出来
			 	ShiroRole role = roleService.selectRoleByid(shiro.getRoleId());
	            authorizationInfo.addRole(role.getRole());
	            for(ShiroMenuPermissionRole p:role.getList()){
	            	//这里我将权限的ID直接放入了权限控制中  可以考虑是否在根据权限的ID去数据库的资源表中将具体的资源或者按钮取到放到这里
	            	//正确的做法是要在这里存入shiro_menu 表中的 permission字段的
	            	System.out.println(p.getMenuId());
	                authorizationInfo.addStringPermission(p.getMenuId().toString());
	            }
	        }
		 return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		// TODO 自动生成的方法存根
		 System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		 String username = (String)token.getPrincipal();
		 System.out.println(token.getCredentials());
		 //通过username从数据库中查找 User对象，如果找到，没找到.
	     //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		 System.out.println("经过这里没啊11");
		 ShiroUser userInfo = loginService.findByUsername(username);
	     System.out.println("经过这里没啊22");
		 if(userInfo == null){
	            return null;
	      }
	     SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
	                userInfo, //用户名
	                userInfo.getPassword(), //密码
	                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
	                getName()  //realm name
	        );
	        return authenticationInfo;
		 
	}

}
