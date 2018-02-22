package io.github.zunpiau.task;

import io.github.zunpiau.domain.Bingwallpaper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BingSpiderTaskTest {

    @Test
    public void serial() throws IOException {
        BingSpiderTask task = new BingSpiderTask();
        Bingwallpaper bingwallpaper = task.serial(new String(Files
                .readAllBytes(Paths
                        .get("target/test-classes/index_bing.html"))));
        System.out.println(bingwallpaper);
//        assertEquals("Cvandyke/Shutterstock", bingwallpaper.getCopyright());
//        assertEquals("https://cn.bing.com/az/hprichbg/rb/AyuttayaBuddha_EN-US8837500887_1920x1080.jpg", bingwallpaper.getCopyright());
//        assertEquals("Thomas Jefferson Memorial reflected in the Tidal Basin, Washington, DC", bingwallpaper.getTitle());
    }
}