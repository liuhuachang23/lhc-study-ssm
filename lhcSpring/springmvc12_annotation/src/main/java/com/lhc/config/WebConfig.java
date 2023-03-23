package com.lhc.config;

import com.lhc.interceptor.FirstInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;
import java.util.Properties;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/18 20:26
 * @ClassName:
 * @Description: 代替SpringMVC的配置文件
 * 1.扫描组件
 * 2.视图解析器
 * 3.配置默认的servlet
 * 4.开启mvc的注解驱动
 * 5.视图控制器
 * 6.文件上传解析器
 * 7.拦截器
 * 8.异常解析器
 */
@Configuration //将类标识为配置类
@ComponentScan("com.lhc.controller") //1、扫描组件
@EnableWebMvc //4.开启mvc的注解驱动
public class WebConfig implements WebMvcConfigurer {

    //3. 配置默认的servlet,处理静态资源
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //5.视图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //添加视图控制器，设置处理的请求地址 和 设置请求地址所对应的视图名称
        registry.addViewController("/").setViewName("index");
    }

    //6.文件上传解析器
    @Bean // 注解@Bean 可以将标识方法的返回值作为bean进行管理，方法名作为bean的id
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

    //7.拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建一个拦截器
        FirstInterceptor firstInterceptor = new FirstInterceptor();
        //添加拦截器，指定需要拦截的请求 和不需要拦截的请求
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**").excludePathPatterns();
    }

    //8.异常解析器
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        //创建一个自定义异常处理器
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        //创建一个Properties
        Properties prop = new Properties();
        //key:异常类型 value：出现该异常需要跳转的逻辑视图
        prop.setProperty("java.lang.ArithmeticException","error");
        //设置ex为请求域中异常信息的属性名
        exceptionResolver.setExceptionAttribute("ex");

        //将把我们创建的异常处理器添加到 该方法的参数中（参数为HandlerExceptionResolver类型的list集合）
        resolvers.add(exceptionResolver);
    }


    //2.视图解析器
    //1) 配置 生成模板解析器
    @Bean
    public ITemplateResolver templateResolver() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        // ServletContextTemplateResolver需要一个ServletContext作为构造参数，可通过WebApplicationContext 的方法获得
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(webApplicationContext.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    //2) 生成模板引擎 并为模板引擎 注入模板解析器
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    //3)生成视图解析器 并未解析器 注入模板引擎
    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

}
