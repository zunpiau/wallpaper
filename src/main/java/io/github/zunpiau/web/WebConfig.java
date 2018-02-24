package io.github.zunpiau.web;

import io.github.zunpiau.web.Interceptor.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "io.github.zunpiau.web.controller")
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

    @Value("${auth.name}")
    private String headerName;
    @Value("${auth.vaule}")
    private String headerVaule;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderInterceptor(headerName, headerVaule))
                .addPathPatterns("/status", "/status/*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setSupportedMediaTypes(
                Collections.singletonList(
                        MediaType.parseMediaType(MediaType.TEXT_HTML_VALUE)));
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(
                MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE)));
        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
    }

}
