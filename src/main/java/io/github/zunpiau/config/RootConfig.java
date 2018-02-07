package io.github.zunpiau.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Import({DataConfig.class, LogbackConfig.class, WebConfig.class, CacheConfig.class})
@ComponentScan(basePackages = {"io.github.zunpiau"})
@EnableScheduling
public class RootConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public ScheduledExecutorService service() {
        return new ScheduledThreadPoolExecutor(2);
    }

    @Bean
    public TaskScheduler scheduler() {
        return new ThreadPoolTaskScheduler();
    }

}
