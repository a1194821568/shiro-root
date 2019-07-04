package com.zhanghui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger {

	@Bean
	public Docket createRestApi(){
		return new Docket(DocumentationType.SWAGGER_2)
		                  .apiInfo(apiInfo())
		                  .select()
		                  .apis(RequestHandlerSelectors.basePackage("com.zhanghui.controller")) //这里要配置自己的controller的包名 否则swagger不会显示方法
		                  .paths(PathSelectors.any())
		                  .build();
	}

	private ApiInfo apiInfo() {
		// TODO 自动生成的方法存根
		return new ApiInfoBuilder()
		//页面标题
        .title("Course 项目 API 开发文档")
          //创建人
	    .contact(new Contact("啦啦啦 假的 假的 假的", "http://www.wangzijiang.cn", "123@qq.com"))
	     //描述
		.description("简单优雅的restfun风格")
		//服务条款URL
		.termsOfServiceUrl("http://blog.csdn.net")
		 //版本号
		.version("1.0")
		.build();
	}
}