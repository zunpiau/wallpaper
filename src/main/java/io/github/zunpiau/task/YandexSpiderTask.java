package io.github.zunpiau.task;

import io.github.zunpiau.dao.YandexRepository;
import io.github.zunpiau.domain.YandexWallpaper;
import io.github.zunpiau.serialize.YandexDeserializer;
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

@Component
@PropertySource("classpath:application.properties")
public class YandexSpiderTask extends BaseSpiderTask<YandexWallpaper, YandexRepository> {

    private YandexDeserializer deserializer;

    public YandexSpiderTask() {
        logger = LoggerFactory.getLogger(this.getClass());
        this.request = new Request.Builder()
                .get()
                .url("http://www.yandex.com/images/")
                .addHeader("User-Agnet", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.40 Safari/537.36")
                .build();
    }

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Autowired
    public void setDeserializer(YandexDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Autowired
    public void setRepository(YandexRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "${cron.yandex}", zone = "Asia/Shanghai")
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

    protected String pickup(String page) {
        int start = page.indexOf("{\"today\":");
        int i = start;
        for (int countBraces = 0; i < page.length(); i++) {
            if (page.charAt(i) == '{')
                countBraces++;
            else if (page.charAt(i) == '}')
                countBraces--;
            if (countBraces == 0)
                break;
        }
        String json = page.substring(start, i + 1);
        logger.info(json);
        return json;
    }

    @Override
    protected YandexWallpaper serial(String s) throws IOException {
        return deserializer.convert(pickup(s));
    }

    @Override
    protected void save(YandexWallpaper wallpaper) {
        repository.save(wallpaper);
    }

}
