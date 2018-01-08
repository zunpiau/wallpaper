package io.github.zunpiau.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.serialize.YandexDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SpiderTaskTest {

    @Test
    public void pickup() throws IOException {
        SpiderTask task = new SpiderTask();
        assertEquals("2017-12-15", new YandexDeserializer(new ObjectMapper())
                .convert(task
                        .pickup(new String(Files
                                .readAllBytes(Paths
                                        .get("target/test-classes/index.html")))))
                .getDate());
    }

}