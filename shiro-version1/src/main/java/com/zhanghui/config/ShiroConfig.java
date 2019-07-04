package com.zhanghui.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration// 等价于beans
public class ShiroConfig {
	/**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     * 
     * 参考：https://www.jianshu.com/p/d07b251485ac
     */
	
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		//配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");

		//未授权界面; 这里可以自定义 不一定要叫403  只要有相应的页面 叫error 或其他都可以
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 凭证匹配器
	 * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * ）
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher(){
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
		//hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64；
		return hashedCredentialsMatcher;
	}

	/**
	 * Realm实现
	 * 
	 * @return
	 */
	@Bean
	public MyShiroRealm myShiroRealm(){
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	/**
	 * 会话管理器
	 * 
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myShiroRealm());
		  // 自定义缓存实现 使用redis
        //securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        //securityManager.setSessionManager(sessionManager());
        //注入记住我管理器; 
        //securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}
	
	
	//-----------------------------------------------------------------------------------------
	//下面这些注释掉的是从网上找来的 没有经过测试 所以都注释掉了  有待进一步研究
	/*@Bean
	public MyShiroSessionListener myShiroSessionListener() {
		return new MyShiroSessionListener();//需要自己创建
	}
 
	*//**
	 * 会话监听器
	 * 
	 * @return
	 *//*
	@Bean
	public Collection<SessionListener> sessionListeners() {
		Collection<SessionListener> listeners = new ArrayList<>();
		listeners.add(myShiroSessionListener());
		return listeners;
	}
 
	*//**
	 * 会话ID生成器
	 * 
	 * @return
	 *//*
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		SessionIdGenerator idGenerator = new SessionIdGenerator() {
			@Override
			public Serializable generateId(Session session) {
				Serializable uuid = new JavaUuidSessionIdGenerator().generateId(session);
				System.out.println("sessionIdGenerator:" + uuid);
				return uuid;
			}
		};
		return idGenerator;
	}
 
	*//**
	 * 会话DAO
	 * 
	 * @return
	 *//*
	@Bean
	public MySessionDao mySessionDao() {
		System.out.println("ShiroConfiguration.mySessionDao()");
		MySessionDao mySessionDao = new MySessionDao();
		mySessionDao.setActiveSessionsCacheName("shiro-activeSessionCache");
		mySessionDao.setSessionIdGenerator(sessionIdGenerator());
		return mySessionDao;
	}*/
	

	/**
	 * 处理session有效期
	 * 
	 * @return
	 */ 
		@Bean
		public ExecutorServiceSessionValidationScheduler sessionValidationScheduler() {
			ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
			sessionValidationScheduler.setInterval(1800);
			return sessionValidationScheduler;
		}
	
	
	 /**
     * cookie对象;
     * @return
    
	    public SimpleCookie rememberMeCookie(){
	        System.out.println("cookie11111");
	       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
	       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	       //<!-- 记住我cookie生效时间30天 ,单位秒;-->
	       simpleCookie.setMaxAge(2592000);
	       return simpleCookie;
	    } 
	 */
	
	 /**
     * cookie管理对象;记住我功能
     * @return
     
	    public CookieRememberMeManager rememberMeManager(){
	        System.out.println("cookieManager11111");
	       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	       cookieRememberMeManager.setCookie(rememberMeCookie());
	       //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
	       cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
	       return cookieRememberMeManager;
	    }
	*/
	/**
	 * Shiro生命周期处理器 ---可以自定的来调用配置在 Spring IOC 容器中 shiro bean 的生命周期方法.
	 * 
	 * @return
	 
		@Bean
		public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
			return new LifecycleBeanPostProcessor();
		}
	 */
	

	/**
	 * 开启shiro注解 ----启用 IOC 容器中使用 shiro 的注解. 但必须在配置了 LifecycleBeanPostProcessor
	 * 之后才可以使用
	 * 
	 * @return
	 
		@Bean
		@DependsOn("lifecycleBeanPostProcessor")
		public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
			DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
			daap.setProxyTargetClass(true);
			return daap;
		}
	 */
	//-----------------------------------------------------------------------------------------
	
	
	/**
	 *  开启shiro aop注解支持.
	 *  使用代理方式;所以需要开启代码支持;
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean(name="simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
		mappings.setProperty("UnauthorizedException","403");
		r.setExceptionMappings(mappings);  // None by default
		r.setDefaultErrorView("error");    // No default
		r.setExceptionAttribute("ex");     // Default is "exception"
		//r.setWarnLogCategory("example.MvcLogger");     // No default
		return r;
	}
}