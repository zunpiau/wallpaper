package io.github.zunpiau.web.controller;

import io.github.zunpiau.config.RootConfig;
import io.github.zunpiau.dao.YandexRepository;
import io.github.zunpiau.domain.YandexWallpaper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class YandexControllerTest {

    private YandexRepository repository = mock(YandexRepository.class);
    private YandexController controller = new YandexController(repository);

    @Test
    public void daily() throws Exception {
        when(repository.getLastUrl()).thenReturn("https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/yandex"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig"));
    }

    @Test
    public void date() throws Exception {
        YandexWallpaper wallpaper = new YandexWallpaper("2017-12-17",
                "https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig",
                "Parallel worlds",
                "A frosty morning at Dzhangyskol lake in the Altai Mountains, Russia.",
                "Vladislav Sokolovsky",
                "http://photo.rgo.ru/ru",
                "РГО",
                "IBcSFw"
        );
        when(repository.getWallpaper("2017-12-17")).thenReturn(wallpaper);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/yandex/2017-12-17"))
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":200,\"data\":{\"info\":\"2017-12-17\",\"url\":\"https://avatars.mds.yandex.net/get-imageoftheday/103124/d93cd1b36a5d45a0ab5728f39a2d4bcb/orig\",\"title\":\"Parallel worlds\",\"description\":\"A frosty morning at Dzhangyskol lake in the Altai Mountains, Russia.\",\"authorName\":\"Vladislav Sokolovsky\",\"authorLink\":\"http://photo.rgo.ru/ru\",\"partner\":\"РГО\",\"hashDate\":\"IBcSFw\"}}"));

    }
}