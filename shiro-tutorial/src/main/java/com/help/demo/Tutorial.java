package com.help.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
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
	        
	        //用户未知情况下调用 
             signUp("这里可以传用户");//关于用户已知未知请参考下面Amazon的例子
             
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
	        
	        //这段是将上面3个步骤在程序中配置 需要自己定义一个Realm类 这样就可以直接在程序中写代码不用配置 ini文件了 
	       /* Realm realm = //实例化或获得一个Realm的实例。我们将稍后讨论Realm。

    		SecurityManager securityManager = new DefaultSecurityManager(realm);

    		//使SecurityManager实例通过静态存储器对整个应用程序可见：
    		SecurityUtils.setSecurityManager(securityManager);*/
	        
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
	        	//最大权限root secret
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
	            
	            //登录成功后的一些操作
	            //查看一个 Subject 是否有特定（单独）的角色，你可以调用subject.hasRole(roleName))方法，做出相应的反馈。
	            if (currentUser.hasRole("admin")) {
	                //显示 admin 按钮
	            	System.out.println("有这个角色");
	            } else {
	                //不显示按钮?  灰色吗？
	            	System.out.println("无这个角色");
	            }
	            
	            //你可以在的代码执行之前简单判断他们是否是所要求的角色，如果 Subject 不是所要求的角色， 
	            //AuthorizationException 异常将被抛出，如果是所要求的角色，判断将安静地执行并按期望顺序执行下面的逻辑。
	            //保证当前用户是一个银行出纳员
	            //因此允许开立帐户：
	            currentUser.checkRole("bankTeller");//控制台如果出现这个错误就是没有权限 Subject does not have role [bankTeller]
	          //openBankAccount();这个是你自己的方法
	            
	            
	            // 备注 PrinterPermission这个需要自己新建一个类 报错时直接选择创建 并将添加构造函数
	            //例如，假设一下以下情景：办公室里有一台唯一标识为 laserjet4400n 的打印机，
	            //在我们向用户显示打印按钮之前，软件需要检查当前用户是否允许用这台打印机打印文档，检查权限的方式会是这样
	            Permission printPermission = new PrinterPermission("laserjet4400n", "print");
	            //这里的权限是要和资源关联的 目前没想到如何实现
	            if (currentUser.isPermitted(printPermission)) {
	                //显示 打印 按钮
	            	System.out.println("有打印权限");
	            } else {
	                //不显示按钮?  灰色吗？
	            	System.out.println("无打印权限");
	            }
	            
	            //用户已知的情况下调用方法
	             updateAccount(null);
	             
	              //当前 Session 中被验证过或者在以前的 Session 中被记住过
	             updateAccount2(null);
	             
	             
	             //用户未知情况下调用
	             signUp(value);
	             
	             //至少要有一个权限 
	             createAccount(null);
	             createAccount2(null);
	             
	             //表示要求当前Subject在执行被注解的方法时具备所有的角色
	             //可以将上面的用户换成有admin权限的 下面才会输出
	             deleteUser(value);
	            
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
	
	//RequiresAuthentication 注解表示在访问或调用被注解的类/实例/方法时，要求 Subject 在当前的 session中已经被验证。
	@RequiresAuthentication
	public static void updateAccount(Account userAccount) {
		//正常这个方法不用静态 只是因为我直接使用main方法调用 才需要加上static
	    //这个方法只会被调用在
	    //Subject 保证被认证的情况下
	    System.out.println("可以调用");
	}
	
	
	//RequiresUser* 注解表示要求在访问或调用被注解的类/实例/方法时，
	//当前 Subject 是一个程序用户，“程序用户”是一个已知身份的 Subject，
	//或者在当前 Session 中被验证过或者在以前的 Session 中被记住过。
	@RequiresUser
	public static void updateAccount2(Account account) {
	    //这个方法只会被 'user' 调用 
	    //i.e. Subject 是一个已知的身份with a known identity
	    System.out.println("当前 Session 中被验证过或者在以前的 Session 中被记住过");
	}
	

	//RequiresGuest 注解表示要求当前Subject是一个“guest(访客)”，也就是，
	//在访问或调用被注解的类/实例/方法时，他们没有被认证或者在被前一个Session 记住。
	@RequiresGuest
	public static void signUp(String newUser) {
		
	    //这个方法只会被调用在
	    //Subject 未知/匿名的情况下
	    System.out.println("用户未知情况下调用");
	}
	
	
	//RequiresRoles 注解表示要求当前Subject在执行被注解的方法时具备所有的角色，否则将抛出 
	@RequiresRoles("admin")
	public static void deleteUser(String user) {
	    //这个方法只会被 administrator 调用 
	    System.out.println("表示要求当前Subject在执行被注解的方法时具备所有的角色");
	}
	
	//RequiresPermissions 注解表示要求当前Subject在执行被注解的方法时具备一个或多个对应的权限
	@RequiresPermissions("account:create")
	public static void createAccount(Account account) {
	    //这个方法只会被调用在
	    //Subject 允许创建一个 account 的情况下
	    System.out.println("RequiresPermissions 注解表示要求当前Subject在执行被注解的方法时具备一个或多个对应的权限。");
	}
	
	//这个这个和上面的注解方法时一样的功能。但输出的现在不一样 上面的可以直接执行 下面的报异常
	public static void createAccount2(Account account) {
	    Subject currentUser = SecurityUtils.getSubject();
	    if (!currentUser.isPermitted("account:create")) {
	        throw new AuthorizationException();
	    }
	    System.out.println("这个和上面的注解方法时一样的功能");
	    //在这里 Subject 确保是允许
	    
	}
}
