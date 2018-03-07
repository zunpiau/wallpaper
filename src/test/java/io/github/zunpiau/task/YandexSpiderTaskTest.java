package io.github.zunpiau.task;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context.xml")
@WebAppConfiguration
@ActiveProfiles("dev")
public class YandexSpiderTaskTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private YandexSpiderTask task;

    @Test
    public void pickup() throws IOException {
        assertEquals(new String(Files.readAllBytes(Paths
                        .get("target/test-classes/index_yandex.json"))),
                task.pickup(new String(Files.readAllBytes(Paths
                        .get("target/test-classes/index_yandex.html")))));
    }

    @Ignore
    @Test
    public void crawl() throws IOException {
        thrown.expect(YandexSpiderTask.InvalidDateException.class);
        task.crawl();
    }

    public static class MethodReplacer implements org.springframework.beans.factory.support.MethodReplacer {

        @Override
        public Object reimplement(Object o, Method method, Object[] objects) throws Throwable {
            return new String(Files.readAllBytes(Paths
                    .get("target/test-classes/yandex_1980.html")));
        }
    }
}