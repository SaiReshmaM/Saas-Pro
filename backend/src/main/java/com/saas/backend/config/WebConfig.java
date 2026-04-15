package com.saas.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward all non-file paths to index.html for SPA routing.
        // We use patterns that specifically exclude segments with dots (extensions).
        
        // Single level: /dashboard, /login
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
                
        // Two levels: /dashboard/settings, /invoices/123
        registry.addViewController("/{path1:[^\\.]*}/{path2:[^\\.]*}")
                .setViewName("forward:/index.html");

        // Three levels: /admin/users/edit
        registry.addViewController("/{path1:[^\\.]*}/{path2:[^\\.]*}/{path3:[^\\.]*}")
                .setViewName("forward:/index.html");
    }

    
}
