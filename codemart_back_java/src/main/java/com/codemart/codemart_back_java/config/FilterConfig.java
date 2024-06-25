//package com.codemart.codemart_back_java.config;
//
//import com.codemart.codemart_back_java.component.JwtAuthenticationTokenFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<JwtAuthenticationTokenFilter> jwtAuthenticationTokenFilter(){
//        FilterRegistrationBean<JwtAuthenticationTokenFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtAuthenticationTokenFilter());
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
//
//}
