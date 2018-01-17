package io.github.zunpiau.task;

import io.github.zunpiau.dao.YandexRepository;
import io.github.zunpiau.serialize.YandexDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpiderTask {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private OkHttpClient client;
    private Request request;
    private YandexDeserializer deserializer;
    private YandexRepository repository;

    @Autowired
    public SpiderTask() {
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

    @Scheduled(cron = "30 0 0 * * *", zone = "Asia/Shanghai")
    public void crawl() {
        logger.info("crawl task start");
        try {
            preform();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("crawl task end");
    }

    private void preform() throws IOException {
        repository.save(deserializer.convert(pickup(request())));
    }

    public String pickup(String page) {
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

    private String request() throws IOException {
        ResponseBody body = client.newCall(request).execute().body();
        if (body == null) {
            throw new IOException();
        }
        return body.string();
    }

}
