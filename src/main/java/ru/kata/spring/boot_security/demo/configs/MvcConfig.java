package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        //Аналоги компонентов @Controller, если нужно просто отдать статику?
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/user").setViewName("profile");
        registry.addViewController("/admin").setViewName("profile");
    }
}
