package io.github.zunpiau.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.domain.Bingwallpaper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class BingSpiderTaskTest {

    @Test
    public void serial() throws IOException {
        BingSpiderTask task = new BingSpiderTask(new ObjectMapper());
        Bingwallpaper bingwallpaper = task.serial(new String(Files
                .readAllBytes(Paths
                        .get("target/test-classes/bing.json"))));
        assertEquals("Black Cat Imaging/AlamyStock Photo", bingwallpaper.getCopyright());
        assertEquals("https://cn.bing.com/az/hprichbg/rb/LanternFestial_ZH-CN13235289391_1920x1080.jpg", bingwallpaper.getUrl());
        assertEquals("【今日元宵节】乐圣岭天后宫悬挂的红灯笼，马来西亚吉隆坡", bingwallpaper.getTitle());
    }
}