package userweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/userimage/**").addResourceLocations("file:D:\\imageschool\\userimages\\");
        registry.addResourceHandler("/activityimage/**").addResourceLocations("file:D:\\imageschool\\adminimages\\");
        System.out.println("路径映射成功");
    }
}
