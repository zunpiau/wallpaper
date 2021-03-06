package io.github.zunpiau.dao;

import io.github.zunpiau.domain.YandexWallpaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class YandexRepository extends BaseRepository<YandexWallpaper> {

    private final static String NAME_AND_KEY_URL = "yandex_lastUrl";
    private final static String NAME_AND_KEY_WALLPAPER = "yandex_lastWallpaper";
    private final String ALL_FILED = " date, url, title, description, author_name, author_link, partner, hash_date ";
    private final String SQL_SELECT_ALL = "SELECT " + ALL_FILED + " FROM yandex ";
    private final String SQL_SELECT_URL = "SELECT url FROM yandex ";
    private final String SQL_ORDER = " ORDER BY id DESC LIMIT 1";
    private final String SQL_WHERE = " WHERE date = ?";
    private final JdbcTemplate template;
    private final CacheManager manager;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public YandexRepository(JdbcTemplate template, CacheManager manager) {
        this.template = template;
        this.manager = manager;
    }

    @Override
    @Cacheable(value = NAME_AND_KEY_URL, key = "'" + NAME_AND_KEY_URL + "'")
    public String getLastUrl() {
        return template.queryForObject(SQL_SELECT_URL + SQL_ORDER, String.class);
    }

    @Override
    @Cacheable(value = NAME_AND_KEY_WALLPAPER, key = "'" + NAME_AND_KEY_WALLPAPER + "'")
    public YandexWallpaper getLastWallpaper() {
        return template.queryForObject(SQL_SELECT_ALL + SQL_ORDER, new YandexRowMapper());
    }

    @Override
    public String getUrl(String date) {
        return template.queryForObject(SQL_SELECT_URL + SQL_WHERE, String.class, date);
    }

    @Override
    public YandexWallpaper getWallpaper(String date) {
        return template.queryForObject(SQL_SELECT_ALL + SQL_WHERE, new YandexRowMapper(), date);
    }

    @Override
    public void save(YandexWallpaper wallpaper) {
        logger.debug(wallpaper.getDate());
        template.update("INSERT INTO yandex( " + ALL_FILED + " ) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                wallpaper.getDate(),
                wallpaper.getUrl(),
                wallpaper.getTitle(),
                wallpaper.getDescription(),
                wallpaper.getAuthorName(),
                wallpaper.getAuthorLink(),
                wallpaper.getPartner(),
                wallpaper.getHashDate());
        manager.getCache(NAME_AND_KEY_URL).put(NAME_AND_KEY_URL, wallpaper.getUrl());
        manager.getCache(NAME_AND_KEY_WALLPAPER).put(NAME_AND_KEY_WALLPAPER, wallpaper);
    }

    static class YandexRowMapper implements RowMapper<YandexWallpaper> {

        @Override
        public YandexWallpaper mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new YandexWallpaper(
                    resultSet.getString("date"),
                    resultSet.getString("url"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("author_name"),
                    resultSet.getString("author_link"),
                    resultSet.getString("partner"),
                    resultSet.getString("hash_date")
            );
        }
    }

}
