package io.github.zunpiau.task;

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

    public BingSpiderTask() {
        logger = LoggerFactory.getLogger(this.getClass());
        this.request = new Request.Builder()
                .get()
                .url("https://cn.bing.com/?ensearch=1")
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
        s = s.replace("\n", "");
        Matcher urlMatcher = Pattern.compile("g_prefetch *= *\\{.*?url: *'(.*?)'.*?\\}")
                .matcher(s);
        urlMatcher.find();
        String url = urlMatcher.group(1);
        url = "https://cn.bing.com" + url.replace("\\/", "/");
        Matcher infoMatcher = Pattern.compile("_H\\.mcImgData *= *\\{.*?copyright\": *\"(.*?)\",.*?\\}")
                .matcher(s);
        infoMatcher.find();
        Matcher copyMatcher = Pattern.compile("(.*?) *\\(© *(.*?)\\)")
                .matcher(infoMatcher.group(1));
        copyMatcher.find();
        return new Bingwallpaper(LocalDate.now().toString(), url, copyMatcher.group(1), copyMatcher.group(2));
    }

    @Override
    protected void save(Bingwallpaper wallpaper) {
        repository.save(wallpaper);
    }

}
