package io.github.zunpiau.dao;

import io.github.zunpiau.config.RootConfig;
import io.github.zunpiau.domain.YandexWallpaper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration()
@ActiveProfiles("dev")
public class YandexRepositoryTest {

    @Autowired
    private YandexRepository repository;
    @Autowired
    private JdbcTemplate template;

    @Before
    public void save() {
        YandexWallpaper wallpaper = new YandexWallpaper("2017-12-17",
                "https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig",
                "Parallel worlds",
                "A frosty morning at Dzhangyskol lake in the Altai Mountains, Russia.",
                "Vladislav Sokolovsky",
                "http://photo.rgo.ru/ru",
                "РГО",
                "IBcSFw"
        );
        repository.save(wallpaper);
    }

    @Test
    public void getLast() {
        assertEquals(repository.getLast(), "https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig");
    }

    @Test
    public void get() {
        assertEquals(repository.get("2017-01-01"), null);
        assertEquals(repository.get("2017-12-17").getHashDate(), "IBcSFw");
    }

    @After
    public void delete() {
        template.update("DELETE  FROM yandex where id IN " +
                        "(SELECT id FROM yandex ORDER BY id DESC LIMIT 1);");
    }

}