package io.github.zunpiau.serialize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zunpiau.domain.YandexWallpaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class YandexDeserializer {

    private ObjectMapper mapper;

    @Autowired
    public YandexDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public YandexWallpaper convert(String source) throws IOException {
        YandexWallpaper wallpaper = new YandexWallpaper();
        JsonNode rootNode = mapper.readTree(source).get("today");
        wallpaper.setUrl("https:" + rootNode.get("slide").get("appropriate").get("url").asText());
        JsonNode snippetNode = rootNode.get("snippet");
        wallpaper.setTitle(snippetNode.get("title").asText());
        wallpaper.setDescription(snippetNode.get("description").asText());
        JsonNode authorNode = rootNode.get("author");
        wallpaper.setAuthorName(authorNode.get("name").asText());
        wallpaper.setAuthorLink(authorNode.get("link").asText());
        wallpaper.setDate(rootNode.get("date").asText());
        wallpaper.setPartner(rootNode.get("partner").asText());
        wallpaper.setHashDate(rootNode.get("hash_date").asText());
        return wallpaper;
    }
}
