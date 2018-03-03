package io.github.zunpiau.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.dao.BingRepository;
import io.github.zunpiau.domain.Bingwallpaper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@PropertySource("classpath:application.properties")
public class BingSpiderTask extends BaseSpiderTask<Bingwallpaper, BingRepository> {

    private ObjectMapper mapper;

    @Autowired
    public BingSpiderTask(ObjectMapper mapper) {
        this.mapper = mapper;
        logger = LoggerFactory.getLogger(this.getClass());
        this.request = new Request.Builder()
                .get()
                .url("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN")
                .addHeader("User-Agnet", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.40 Safari/537.36")
                .build();
    }

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Autowired
    public void setRepository(BingRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "${cron.bing}", zone = "Asia/Shanghai")
    @Retryable(value = IOException.class,
            backoff = @Backoff(3000))
    public void crawl() throws IOException {
        logger.info("crawl task start");
        try {
            save(serial(request()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
        logger.info("crawl task end");
    }

    @Override
    protected String request() throws IOException {
        ResponseBody body = client.newCall(request).execute().body();
        if (body == null) {
            throw new IOException();
        }
        return body.string();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected Bingwallpaper serial(String s) throws IOException {
        JsonNode root = mapper.readTree(s)
                .get("images")
                .get(0);
        String url = root.get("url").asText();
        String info = root.get("copyright").asText();
        Matcher copyMatcher = Pattern.compile("^(.*)\\(© *(.*)[)）]$").matcher(info);
        copyMatcher.find();
        return new Bingwallpaper(LocalDate.now().toString(),
                "https://cn.bing.com" + url,
                copyMatcher.group(1).trim(),
                copyMatcher.group(2));
    }

    @Override
    protected void save(Bingwallpaper wallpaper) {
        repository.save(wallpaper);
    }

}
