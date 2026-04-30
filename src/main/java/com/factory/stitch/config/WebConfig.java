package com.factory.stitch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static resources from classpath - NO FORWARDING
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
        
        // Specific resource mappings for direct access
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/login_register/**")
                .addResourceLocations("classpath:/static/login_register/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/hr_dashboard/**")
                .addResourceLocations("classpath:/static/hr_dashboard/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/production_dashboard/**")
                .addResourceLocations("classpath:/static/production_dashboard/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/stock_dashboard/**")
                .addResourceLocations("classpath:/static/stock_dashboard/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/purchasing_dashboard/**")
                .addResourceLocations("classpath:/static/purchasing_dashboard/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/it_dashboard/**")
                .addResourceLocations("classpath:/static/it_dashboard/")
                .setCachePeriod(0);
                
        registry.addResourceHandler("/finance_dashboard/**")
                .addResourceLocations("classpath:/static/finance_dashboard/")
                .setCachePeriod(0);
    }

    // REMOVED: addViewControllers - No more infinite forward loops
    // Spring Boot will automatically serve index.html from static resources
}
