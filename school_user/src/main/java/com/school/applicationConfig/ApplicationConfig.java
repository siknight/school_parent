package com.school.applicationConfig;


import com.school.JwtFilter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 将出login以外的所有请求都经过该jwtFilter，然后通过jwtFilter解析出token
 */
@Component
public class ApplicationConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter).
                addPathPatterns("/**").
                excludePathPatterns("/**/login")  //允许通过 登录
                .excludePathPatterns("/**/sendsms")   //发送验证码通过
                .excludePathPatterns("/**/pass/**")   //密码修改验证通过
                .excludePathPatterns("/**/register/**")//修改密码
                .excludePathPatterns("/token/**")
                .excludePathPatterns("/article/findIndexArticle");  //token验证

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }
}
