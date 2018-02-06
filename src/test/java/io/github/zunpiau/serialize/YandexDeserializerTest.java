package io.github.zunpiau.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.domain.YandexWallpaper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class YandexDeserializerTest {

    @Test
    public void convert() throws IOException {
        YandexWallpaper wallpaper = new YandexDeserializer(
                new ObjectMapper()).convert(
                new String(Files.readAllBytes(Paths.get("target/test-classes/index.json"))));
        assertEquals("IBcSFQ", wallpaper.getHashDate());
        assertEquals("2017-12-15", wallpaper.getDate());
        assertEquals("https://avatars.mds.yandex.net/get-imageoftheday/142379/bd68250fb3d241b390f8442dd5f06a80/orig", wallpaper.getUrl());
    }
}