package com.help.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.mgt.SecurityManager;

public class Tutorial {
	private static final transient Logger log = LoggerFactory.getLogger(Tutorial.class);

	public static void main(String[] args) {
	
	        log.info("My First Apache Shiro Application");
	        
	        //1.使用 Shiro 的 IniSecurityManagerFactory 加载了我们的shiro.ini 文件，
	        //该文件存在于 classpath 根目录里。这个执行动作反映出 shiro 支持 
	        //Factory Method Design Pattern（工厂模式）。classpath：资源的指示前缀，
	        //告诉 shiro 从哪里加载 ini 文件（其它前缀，如 url:和 file: 也被支持）。
	        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

	        //2.factory.getInstance() 方法被调用，该方法分析 INI 文件并根据配置文件返回一个 SecurityManager 实例。
	        SecurityManager securityManager = factory.getInstance();

	        //3.在这个简单示例中，我们将 SecurityManager 设置成了static (memory) singleton，
	        //可以通过 JVM 访问，注意如果你在一个 JVM 中加载多个使用 shiro 的程序时不要这样做，
	        //在这个简单示例中，这是可以的，但在其它成熟的应用环境中，通常会将 SecurityManager 
	        //放在程序指定的存储中（如在 web 中的 ServletContexct 或者 Spring、Guice、 JBoss DI 容器实例）中。
	        SecurityUtils.setSecurityManager(securityManager);
	        
	        //在几乎所有的环境中，你可以通过如下语句得到当前用户的信息：
	        //Subject是一个安全术语意思是“当前运行用户的指定安全视图（a security-specific view of the currently executing user）”
	        //安全认证中，“Subject”可以认为是一个人，也可以认为是第三方进程、时钟守护任务、
	        //守护进程帐户或者其它。它可简单描述为“当前和软件进行交互的事件”，
	        //在大多数情况下，你可以认为它是一个“人（User）”。
	        Subject currentUser = SecurityUtils.getSubject();
	        
	        // 做点跟 Session 相关的事
	        Session session = currentUser.getSession();
	        session.setAttribute( "someKey", "aValue" );
	        String value = (String) session.getAttribute("someKey");
	        if (value.equals("aValue")) {
	            System.out.println("Retrieved the correct value! [" + value + "]");
	        }
	        
	        if ( !currentUser.isAuthenticated() ) {
	            //收集用户的主要信息和凭据，来自GUI中的特定的方式
	            //如包含用户名/密码的HTML表格，X509证书，OpenID，等。
	            //我们将使用用户名/密码的例子因为它是最常见的。
	        	//下面的 lonestarr  vespa 是在配置文件中的用户名和密码 例 username = password, role1, role2, ..., roleN
	            //可以每一个用户都试一试看看有什么不同
	        	UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");

	            //支持'remember me' (无需配置，内建的!):
	            token.setRememberMe(true);
	            //
	           
	        try{
	            currentUser.login(token);
	            //无异常，说明就是我们想要的!
	            System.out.println("//无异常，说明就是我们想要的!");
	        } catch ( UnknownAccountException uae ) {
	        	System.out.println(" //username 不存在，给个错误提示?");
	            //username 不存在，给个错误提示?
	        } catch ( IncorrectCredentialsException ice ) {
	        	System.out.println(" //password 不匹配，再输入?");
	            //password 不匹配，再输入?
	        } catch ( LockedAccountException lae ) {
	        	System.out.println(" //账号锁住了，不能登入。给个提示? ");
	            //账号锁住了，不能登入。给个提示? 
	            //... 更多类型异常 ...
	        } catch ( AuthenticationException ae ) {
	            //未考虑到的问题 - 错误?
	        	System.out.println(" //未考虑到的问题 - 错误?");
	        }
	        }
	        //测试用户是谁
	        System.out.println("User [" + currentUser.getPrincipal() + "] logged in successfully.");
	       
	      //测试角色:
	        if (currentUser.hasRole("schwartz")) {
	            System.out.println("May the Schwartz be with you!");
	        } else {
	            System.out.println("Hello, mere mortal.");
	        }
	        
	        //测试一个权限 (非（instance-level）实例级别)
	        //判断他们是否拥有某个特定动作或入口的权限：
	        if (currentUser.isPermitted("lightsaber:weild")) {
	        	System.out.println("You may use a lightsaber ring.  Use it wisely.");
	        } else {
	        	System.out.println("Sorry, lightsaber rings are for schwartz masters only.");
	        }

	        //一个(非常强大)的实例级别的权限:
	        //我们还可以执行非常强大的 instance-level （实例级别）的权限检测，检测用户是否具备访问某个类型特定实例的权限：
	        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
	        	System.out.println("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
	                    "Here are the keys - have fun!");
	        } else {
	        	System.out.println("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
	        }

	        //完成 - 退出t!
	        currentUser.logout();//清楚识别信息，设置 session 失效.
	        
	        System.exit(0);
	}

}
