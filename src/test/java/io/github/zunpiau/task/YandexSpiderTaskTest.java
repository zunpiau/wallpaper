package io.github.zunpiau.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.serialize.YandexDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class YandexSpiderTaskTest {

    @Test
    public void pickup() throws IOException {
        YandexSpiderTask task = new YandexSpiderTask();
        assertEquals("2017-12-15", new YandexDeserializer(new ObjectMapper())
                .convert(task
                        .pickup(new String(Files
                                .readAllBytes(Paths
                                        .get("target/test-classes/index_yandex.html")))))
                .getDate());
    }

}