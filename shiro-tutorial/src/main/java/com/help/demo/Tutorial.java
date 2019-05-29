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
	        	/*Shiro 支持在登录过程中执行"remember me"，在此值得指出，一个已记住的 
	        	 * Subject（remembered Subject）和一个正常通过认证的 Subject
	        	 * （authenticated Subject）在 Shiro 是完全不同的。
				 *
				 *记住的（Remembered）：一个被记住的 Subject 不不会是匿名的，拥有一个已知的身份
				 *也就是说subject.getPrincipals())返回非空）。它的身份被先前的认证过程所记住，
				 *并存于先前session中，一个被认为记住的对象在执行subject.isRemembered())返回true。
				 *
				 *已验证（Authenticated）：一个被验证的 Subject 是成功验证后（如登录成功）
				 *并存于当前 session 中，一个被认为验证过的对象调用subject.isAuthenticated()) 
				 *将返回true。
				 *
				 *
				 *已记住（Remembered）和已验证（Authenticated）是互斥的--一个标识值为真另一个就为假，反过来也一样。
	        	 */
	        	 //下面这个API上的例子有助于理解下面这个rememberMe的作用
	        	/**
	        	 *  下面是一个非常常见的场景帮助说明被记住和被验证之间差别为何重要。
				 *	假设你使用Amazon.com，你已经成功登录并且在购物篮中添加了一些书籍，但你由于临时要参加一个会议，匆忙中你忘记退出登录，当会议结束，回家的时间到了，于是你离开了办公室。
				 *	第二天当你回到工作，你意识到你没有完成你的购买动作，于是你回到Amazon.com，这时，Amazon.com“记得”是是，通过你的名字向你打招呼，仍旧给你提供个性化的图书推荐，对于Amazon.com，subject.isRemembered() 将返回真。
				 *	但是当你想访问你帐号的信用卡信息完成图书购买的时候会怎样呢？虽然Amazon.com “记住”了你（isRemembered() == true），它不能担保你就是你（也许是正在使用你计算机的同事）。
				 *	于是，在你执行像使用信用卡信息之类的敏感操作之前，Amazon.com 强制你登录以使他们担保你的身份，在你登录之后，你的身份已经被验证，对于Amazon.com，isAuthenticated()将返回真。
				 *	这类情景经常发生，所以 Shiro 加入了该功能，你可以在你的程序中使用。现在是使用 isRemembered() 还是使用 isAuthenticated() 来定制你的视图和工作流完全取决于你自己，但 Shiro 维护这种状态基础以防你可能会需要。
	             */
	             token.setRememberMe(true);
	            //
	           
	        //Shiro拥有丰富的运行期异常AuthenticationException可以精确标明为何验证失败，
	        //你可以将 login 放入到 try/catch 块中并捕获所有你想捕获的异常并对它们做出处理    
	        try{
	        	//当身份和证明住处被收集并实例化为一个 AuthenticationToken （认证令牌）后，
	        	//我们需要向 Shiro 提交令牌以执行真正的验证尝试：
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
	        /*Web 程序需注意
	         * 
  			 *因为在 Web 程序中记住身份信息往往使用 Cookies，
  			 *而 Cookies 只能在 Response 提交时才能被删除，
  			 *所以强烈要求在为最终用户调用subject.logout() 之后立即将用户引导到一个新页面，
  			 *确保任何与安全相关的 Cookies 如期删除，
  			 *这是 Http 本身 Cookies 功能的限制而不是 Shiro 的限制。
  			 */
	        currentUser.logout();//清除验证信息，使 session 失效
	        
	        System.exit(0);
	}

}
