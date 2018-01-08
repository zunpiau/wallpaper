package io.github.zunpiau.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Import({LogbackConfigDev.class, LogbackConfigProd.class})
public class LogbackConfig {

    @Bean
    public static ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    public static LoggerContext loggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static PatternLayoutEncoder encoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern("%d{yy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger - %msg%n");
        return encoder;
    }

}
